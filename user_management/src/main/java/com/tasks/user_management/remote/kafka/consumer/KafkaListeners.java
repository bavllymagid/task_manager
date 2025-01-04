package com.tasks.user_management.remote.kafka.consumer;

import com.tasks.user_management.remote.kafka.payload.AuthenticatedUserPayload;
import com.tasks.user_management.remote.kafka.payload.ValidateUserPayload;
import com.tasks.user_management.remote.kafka.producer.TopicsNames;
import com.tasks.user_management.remote.services.RefreshTokenService;
import com.tasks.user_management.utils.exceptions.UserNotFoundException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {
    private final RefreshTokenService refreshTokenService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public KafkaListeners(RefreshTokenService refreshTokenService,
                          KafkaTemplate<String, Object> kafkaTemplate) {
        this.refreshTokenService = refreshTokenService;
        this.kafkaTemplate = kafkaTemplate;
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
}