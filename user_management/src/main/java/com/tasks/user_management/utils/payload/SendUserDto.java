package com.tasks.user_management.utils.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class SendUserDto {
    private BigInteger id;
    private String username;
    private String email;
    private List<String> roles;

    public static SendUserDto fromJson(String json) {
        JSONObject jsonObject = new JSONObject(json);
        return new SendUserDto(jsonObject.getBigInteger("id"), jsonObject.getString("username"), jsonObject.getString("email"), jsonObject.getJSONArray("roles").toList().stream().map(Object::toString).collect(Collectors.toList()));
    }
}
