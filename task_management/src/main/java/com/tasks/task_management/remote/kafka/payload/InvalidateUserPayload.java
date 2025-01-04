package com.tasks.task_management.remote.kafka.payload;

import org.json.JSONObject;

public record InvalidateUserPayload(String id) {
    public static InvalidateUserPayload fromJson(String json) {
        JSONObject jsonObject = new JSONObject(json);
        return new InvalidateUserPayload(jsonObject.getString("id"));
    }
}
