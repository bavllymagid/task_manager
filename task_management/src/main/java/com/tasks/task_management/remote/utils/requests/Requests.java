package com.tasks.task_management.remote.utils.requests;
import com.tasks.task_management.local.StaticObjects.UserSingleton;
import com.tasks.task_management.local.exceptions.InvalidToken;
import com.tasks.task_management.remote.utils.payload.UserInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

public class Requests {
    private static final String BaseUrl = "http://localhost:8080/api/token/";
    private static final String validate = BaseUrl + "validate";
    private static final Logger log = LoggerFactory.getLogger(Requests.class);

    public static boolean validateToken(String token) throws InvalidToken {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(validate))
                .header("Authorization", token)
                .GET()
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            log.debug("Failed to validate token: {}", e.getMessage());
        }

        return Objects.equals(response.body(), "true");
    }
}
