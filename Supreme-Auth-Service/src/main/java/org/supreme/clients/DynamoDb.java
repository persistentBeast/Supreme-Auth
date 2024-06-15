package org.supreme.clients;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamoDb {

    @Bean
    public DynamoDbEnhancedClient ddbClient() {
        Region region = Region.AP_SOUTH_1;
        DynamoDbClient ddb = DynamoDbClient.builder()
                .region(region)
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(System.getenv().get("AWS_ACCESS_KEY_ID"),
                        System.getenv().get("AWS_SECRET_KEY"))))
                .build();
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(ddb)
                .build();
    }

}
