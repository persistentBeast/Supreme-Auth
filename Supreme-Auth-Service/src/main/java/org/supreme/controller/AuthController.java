package org.supreme.controller;

import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.supreme.dao.UserDao;
import org.supreme.exceptions.InvalidRequestException;
import org.supreme.exceptions.ItemExistsException;
import org.supreme.exceptions.ItemNotFoundException;
import org.supreme.exceptions.UnauthorisedRequestException;
import org.supreme.models.User;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;
import software.amazon.awssdk.services.dynamodb.model.DuplicateItemException;

import java.util.Map;
import java.util.UUID;

@RestController()
@RequestMapping("/api/v1")
public class AuthController {

    @Autowired
    UserDao userDao;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/sign-up")
    public ResponseEntity<Object> signUp(@RequestBody Map<String, Object> reqBody) {

        String userName = (String) reqBody.getOrDefault("user_name", "");
        String password = (String) reqBody.getOrDefault("password", "");

        if (StringUtils.isBlank(userName) ||
                StringUtils.isBlank(password)) throw new InvalidRequestException("Invalid Request");

        String userId = UUID.randomUUID().toString();
        String passwordHash = bCryptPasswordEncoder.encode(password);

        try {
            User user = new User(userId, userName, passwordHash);
            userDao.insert(user);
            return ResponseEntity.ok(Map.of("message", "User signed up!"));
        } catch (ConditionalCheckFailedException e) {
            throw new ItemExistsException("Username is already taken, choose another one!");
        }

    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Map<String, Object> reqBody) {

        String userName = (String) reqBody.getOrDefault("user_name", "");
        String password = (String) reqBody.getOrDefault("password", "");

        if (StringUtils.isBlank(userName) ||
                StringUtils.isBlank(password)) throw new InvalidRequestException("Invalid Request");

        User user = userDao.fetch(userName);

        if(ObjectUtils.isEmpty(user)) throw new ItemNotFoundException("User does not exists!");

        boolean isPassValid = bCryptPasswordEncoder.matches(password, user.getPasswordHash());

        if(!isPassValid){
            throw new UnauthorisedRequestException("User not authorised!");
        }

        return ResponseEntity.ok(Map.of("message", "User logged in!"));

    }


}
