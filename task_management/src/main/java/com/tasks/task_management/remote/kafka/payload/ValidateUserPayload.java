package com.tasks.task_management.remote.kafka.payload;

import org.json.JSONObject;

public record ValidateUserPayload(String correlationId, String token) {
    public static ValidateUserPayload fromJson(String value) {
        JSONObject jsonObject = new JSONObject(value);
        return new ValidateUserPayload(
                jsonObject.getString("correlationId"),
                jsonObject.getString("token")
        );
    }
}
