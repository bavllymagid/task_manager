package com.tasks.user_management.remote.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasks.user_management.utils.payload.SendUserDto;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SendUserInstance {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static void sendInstance(SendUserDto user) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        String requestBody = objectMapper.writeValueAsString(user);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create("http://localhost:8081/api/task/receive_instance"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        if (statusCode >= 200 && statusCode < 300) {
            System.out.println("User instance sent successfully");
        } else {
            throw new RuntimeException("Failed to send user instance. Status code: " + statusCode + ", Response: " + response.body());
        }
    }
}
