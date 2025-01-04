package com.tasks.task_management.utils.payload;

import org.json.JSONObject;

import java.math.BigInteger;
import java.util.List;

public record UserInstance(BigInteger id,
                           String username,
                           String email,
                           List<String> roles) {
    public static UserInstance fromJson(String value) {
        JSONObject jsonObject = new JSONObject(value);
        return new UserInstance(
                jsonObject.getBigInteger("id"),
                jsonObject.getString("username"),
                jsonObject.getString("email"),
                List.of(jsonObject.getJSONArray("roles").toList().toArray(new String[0])
                )
        );
    }
}
