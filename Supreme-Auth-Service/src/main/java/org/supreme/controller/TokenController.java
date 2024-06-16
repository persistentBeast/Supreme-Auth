package org.supreme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.supreme.exceptions.InvalidRequestException;
import org.supreme.service.TokenService;

import java.util.Map;

@RestController()
@RequestMapping("/api/v1/token")
public class TokenController {

  @Autowired TokenService tokenService;

  @GetMapping("/validate")
  public ResponseEntity<Object> validateToken(
      @RequestParam(name = "token") String token, @RequestParam(name = "userId") String userId) {
    if (StringUtils.isEmpty(token)) throw new InvalidRequestException("Token is blank!");
    if (StringUtils.isEmpty(userId)) throw new InvalidRequestException("User Id is blank!");
    boolean isValid = tokenService.isTokenValid(token, userId);
    return ResponseEntity.ok().body(Map.of("res", isValid));
  }
}
