package com.tasks.task_management.remote.utils.requests;
import com.tasks.task_management.local.exceptions.InvalidTokenException;
import com.tasks.task_management.remote.utils.payload.UserInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class Requests {
    private static final String BaseUrl = "http://localhost:8080/api/";
    private static final String validate = BaseUrl + "token/validate";
    private static final String changeRole = BaseUrl + "users/add_role";
    private static final Logger log = LoggerFactory.getLogger(Requests.class);

    public static boolean validateToken(String token) throws InvalidTokenException {
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
            throw new InvalidTokenException("Token validation failed");
        }
        return false;
    }


    public static boolean changeRole(String token, String email, String role) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = Map.of("email", email, "role", role);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        try {
            // Send PUT request
            ResponseEntity<String> response = restTemplate.exchange(changeRole, HttpMethod.PUT, entity, String.class);

            log.info("Response Status: {}", response.getStatusCode());
            log.info("Response Body: {}", response.getBody());

            // Check if the response status is 200 (Success)
            if (response.getStatusCode() == HttpStatus.OK) {
                return true;
            } else {
                log.error("Role change failed with status code: {}", response.getStatusCode());
            }

        } catch (RestClientException e) {
            // Log the error message and the stack trace for debugging
            log.error("Failed to change role: {}", e.getMessage(), e);
        }

        return false;
    }

}
