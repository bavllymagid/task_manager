package com.tasks.user_management.remote.requests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;

public class InvalidateUser {
    private static final String BaseUrl = "http://localhost:8081/api/task/";
    private static final String invalidate = BaseUrl + "Invalidate";
    private static final String delete = BaseUrl + "delete_user/";
    private static final Logger log = LoggerFactory.getLogger(InvalidateUser.class);

    public static boolean invalidateUser(String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization",token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Void> response = restTemplate.exchange(invalidate, HttpMethod.GET, entity, Void.class);

            log.info("Response Status: {}", response.getStatusCode());
            log.info("Response Body: {}", response.getBody());

            if (response.getStatusCode().value() == 200) {
                log.info("User invalidated successfully");
                return true;
            } else {
                log.error("Failed to invalidate user with status code: {}", response.getStatusCode());
            }

        } catch (RestClientException e) {
            log.error("Failed to invalidate user: {}", e.getMessage());

        }
        return false;
    }


    public static boolean deleteUser(String token, BigInteger userId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization",token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Void> response = restTemplate.exchange(delete + userId, HttpMethod.DELETE, entity, Void.class);

            log.info("Response Status: {}", response.getStatusCode());
            log.info("Response Body: {}", response.getBody());

            if (response.getStatusCode().value() == 200) {
                log.info("User deleted successfully");
                return true;
            } else {
                log.error("Failed to delete user with status code: {}", response.getStatusCode());
            }

        } catch (RestClientException e) {
            log.error("Failed to delete user: {}", e.getMessage());

        }
        return false;
    }


}
