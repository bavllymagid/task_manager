package com.tasks.task_management.remote.kafka.payload;

import com.tasks.task_management.utils.payload.UserInstance;
import org.json.JSONObject;

public record AuthenticatedUserPayload(String correlationId, UserInstance user) {
    public static AuthenticatedUserPayload fromJson(String value) {
        JSONObject jsonObject = new JSONObject(value);
        return new AuthenticatedUserPayload(
                jsonObject.getString("correlationId"),
                UserInstance.fromJson(jsonObject.getJSONObject("user").toString())
        );
    }
}
