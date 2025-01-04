package com.tasks.user_management.remote.kafka.consumer;

import com.tasks.user_management.remote.kafka.payload.AuthenticatedUserPayload;
import com.tasks.user_management.remote.kafka.payload.ValidateUserPayload;
import com.tasks.user_management.remote.kafka.producer.TopicsNames;
import com.tasks.user_management.remote.services.RefreshTokenService;
import com.tasks.user_management.remote.services.UserService;
import com.tasks.user_management.utils.exceptions.UserNotFoundException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public KafkaListeners(RefreshTokenService refreshTokenService,
                          KafkaTemplate<String, Object> kafkaTemplate,
                          UserService userService) {
        this.refreshTokenService = refreshTokenService;
        this.kafkaTemplate = kafkaTemplate;
        this.userService = userService;
    }

    @KafkaListener(topics = "validateUser", groupId = "group_id")
    public void listenUserAuthenticated(ConsumerRecord<String, String> record) throws UserNotFoundException {
        ValidateUserPayload token = ValidateUserPayload.fromJson(record.value());
        AuthenticatedUserPayload user = new AuthenticatedUserPayload(token.correlationId(),
                refreshTokenService.getUserByToken(token.token()));
        if(user.user() != null) {
            kafkaTemplate.send(TopicsNames.USER_AUTHENTICATED.getTopicName(), user);
        }
    }

    @KafkaListener(topics = "changeRole", groupId = "group_id")
    public void listenChangeRole(ConsumerRecord<String, String> record) throws UserNotFoundException {
        JSONObject jsonObject = new JSONObject(record.value());
        userService.addRoleToUser(jsonObject.getBigInteger("id"),
                jsonObject.getString("role"));
    }
}