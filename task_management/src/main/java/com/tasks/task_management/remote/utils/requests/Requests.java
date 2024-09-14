package com.tasks.task_management.remote.utils.requests;
import com.tasks.task_management.local.StaticObjects.UserSingleton;
import com.tasks.task_management.local.exceptions.InvalidToken;
import com.tasks.task_management.remote.utils.payload.UserInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

public class Requests {
    private static final String BaseUrl = "http://localhost:8080/api/token/";
    private static final String validate = BaseUrl + "validate";
    private static final Logger log = LoggerFactory.getLogger(Requests.class);

    public static boolean validateToken(String token) throws InvalidToken {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<UserInstance> response = restTemplate.exchange(validate, HttpMethod.GET, entity, UserInstance.class);

            log.info("Response Status: {}", response.getStatusCode());
            log.info("Response Body: {}", response.getBody());

            if (response.getStatusCode().value() == 200) {
                return true;
            } else {
                log.error("Token validation failed with status code: {}", response.getStatusCode());
            }

        } catch (RestClientException e) {
            log.error("Failed to validate token: {}", e.getMessage());
            throw new InvalidToken("Token validation failed");
        }
        return false;
    }
}
