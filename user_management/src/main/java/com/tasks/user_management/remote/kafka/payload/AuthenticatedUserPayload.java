package com.tasks.user_management.remote.kafka.payload;

import com.tasks.user_management.utils.payload.SendUserDto;
import org.json.JSONObject;

public record AuthenticatedUserPayload(String correlationId, SendUserDto user) {
    public static AuthenticatedUserPayload fromJson(String json) {
        JSONObject jsonObject = new JSONObject(json);
        return new AuthenticatedUserPayload(jsonObject.getString("correlationId"),
                SendUserDto.fromJson(jsonObject.getString("user")));
    }
}
