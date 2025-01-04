package com.tasks.user_management.remote.kafka.producer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TopicsNames {
    USER_CREATED("userCreated"),
    USER_UPDATED("userUpdated"),
    USER_DELETED("userDeleted"),
    USER_ROLE_UPDATED("userRoleUpdated"),
    USER_AUTHENTICATED("userAuthenticated"),
    USER_LOGGED_OUT("userLoggedOut");
    private final String topicName;
}
