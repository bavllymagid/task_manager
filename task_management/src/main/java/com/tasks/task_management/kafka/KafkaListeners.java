package com.tasks.task_management.kafka;

import com.tasks.task_management.local.StaticObjects.UserSingleton;
import com.tasks.task_management.remote.utils.payload.UserInstance;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class KafkaListeners {
    @KafkaListener(topics = "userAuthenticated", groupId = "group_id")
    public void listenUserAuthenticated(ConsumerRecord<String, String> record) {
        UserInstance user = UserInstance.fromJson(record.value());
        UserSingleton.setInstance(user);
        System.out.println("Received user authenticated: " + UserSingleton.getInstance().toString());
    }
}
