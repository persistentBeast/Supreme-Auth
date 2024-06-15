package org.supreme.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.supreme.models.User;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;

import java.util.Map;

@Repository
public class UserDao implements DaoInterface<User> {

    @Autowired
    DynamoDbEnhancedClient dbClient;


    @Override
    public void insert(User data) {
        DynamoDbTable<User> mappedTable = dbClient.table("users",
                TableSchema.fromBean(User.class));
        PutItemEnhancedRequest<User> putItemEnhancedRequest = PutItemEnhancedRequest.builder(User.class)
                .item(data)
                .conditionExpression(Expression.builder()
                        .expression("attribute_not_exists(#userName)")
                        .expressionNames(Map.of("#userName", "userName"))
                        .build()
                )
                .build();
        mappedTable.putItem(putItemEnhancedRequest);
    }

    @Override
    public User fetch(String hashKey) {
        DynamoDbTable<User> mappedTable = dbClient.table("users",
                TableSchema.fromBean(User.class));
        return mappedTable.getItem(Key.builder().partitionValue(hashKey).build());
    }
}
