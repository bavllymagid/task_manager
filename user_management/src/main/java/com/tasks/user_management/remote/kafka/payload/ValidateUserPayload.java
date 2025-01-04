package com.tasks.user_management.remote.kafka.payload;

import org.json.JSONObject;

public record ValidateUserPayload(String correlationId, String token) {
    public static ValidateUserPayload fromJson(String json) {
        JSONObject jsonObject = new JSONObject(json);
        return new ValidateUserPayload(jsonObject.getString("correlationId"), jsonObject.getString("token"));
    }
}
