package com.tasks.authentication.services;

import com.tasks.authentication.repositories.UserRepository;
import com.tasks.authentication.utils.exceptions.AuthenticationFailedException;
import com.tasks.authentication.utils.exceptions.TokenValidationException;
import com.tasks.authentication.utils.exceptions.UserAlreadyExistsException;
import com.tasks.authentication.utils.payload.RegisteredUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService{
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${auth0.management.api.clientId}")
    private String managementApiClientId;

    @Value("${auth0.management.api.clientSecret}")
    private String managementApiClientSecret;

    @Value("${auth0.domain}")
    private String domain;

    @Value("${auth0.audience}")
    private String audience;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void createUser(RegisteredUser user, String role) throws UserAlreadyExistsException {
        String registrationUrl = "https://" + domain + "/dbconnections/signup";
        Map<String, Object> registrationBody = createRegistrationBody(user);

        try {
            Map<String, Object> response = sendPostRequest(registrationUrl, registrationBody);
            String userId = (String) response.get("_id");
            assignRole(role, "auth0|"+userId);
        } catch (HttpClientErrorException e) {
            handleRegistrationException(e, user);
        }
    }

    private Map<String, Object> createRegistrationBody(RegisteredUser user) {
        Map<String, Object> registrationBody = new HashMap<>();
        registrationBody.put("client_id", managementApiClientId);
        registrationBody.put("email", user.getEmail());
        registrationBody.put("password", user.getPassword());
        registrationBody.put("connection", "Username-Password-Authentication");
        registrationBody.put("username", user.getUsername());

        Map<String, String> userMetadata = new HashMap<>();
        userMetadata.put("username", user.getUsername());
        userMetadata.put("created_at", LocalDateTime.now().toString());
        userMetadata.put("updated_at", LocalDateTime.now().toString());
        registrationBody.put("user_metadata", userMetadata);

        return registrationBody;
    }

    private void handleRegistrationException(HttpClientErrorException e, RegisteredUser user) throws UserAlreadyExistsException {
        String errorResponse = e.getResponseBodyAsString();
        System.err.println("Error during user registration: " + errorResponse);
        log.debug("Error during user registration: {}", errorResponse);

        if (errorResponse.contains("User already exists")) {
            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists.");
        }
        throw new RuntimeException("Failed to register user: " + e.getMessage(), e);
    }

    private Map sendPostRequest(String url, Map<String, Object> body) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, requestEntity, Map.class);
        return responseEntity.getBody();
    }

    private void assignRole(String role, String userId) {
        String roleId = getRoleId(role);
        if (roleId == null) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }

        String managementApiToken = getManagementApiToken();
        String assignRoleUrl = "https://" + domain + "/api/v2/users/" + userId + "/roles";

        Map<String, Object> roleBody = new HashMap<>();
        roleBody.put("roles", List.of(roleId));

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + managementApiToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(roleBody, headers);

        try {
            new RestTemplate().exchange(assignRoleUrl, HttpMethod.POST, requestEntity, Map.class);
            System.out.println("Role assigned successfully to user with ID: " + userId);
        } catch (HttpClientErrorException e) {
            handleRoleAssignmentException(e);
        }
    }

    private void handleRoleAssignmentException(HttpClientErrorException e) {
        System.err.println("Error during role assignment: " + e.getResponseBodyAsString());
        log.debug("Error during role assignment: {}", e.getResponseBodyAsString());
        throw new RuntimeException("Failed to assign role: " + e.getMessage(), e);
    }

    private String getRoleId(String roleName) {
        // retrieve the role ID by its name from Auth0

        return switch (roleName.toLowerCase()) {
            case "user" -> "rol_IL3OO4dvze7QIk0Q";
            case "admin" -> "rol_T1sIeeKuQabcxcJ7";
            default -> null;
        };
    }

    @Override
    public Map<String, Object> authenticateUser(String email, String password) throws AuthenticationFailedException {
        String authUrl = "https://" + domain + "/oauth/token";
        Map<String, Object> body = new HashMap<>();
        body.put("grant_type", "client_credentials");
        body.put("client_id", managementApiClientId);
        body.put("client_secret", managementApiClientSecret);
        body.put("username", email);
        body.put("password", password);
        body.put("audience", audience);

        try {
            Map response = sendPostRequest(authUrl, body);

            if (response != null && response.containsKey("access_token")) {
                Map<String, Object> result = new HashMap<>();
                result.put("token", response.get("access_token"));
                return result;
            } else {
                throw new AuthenticationFailedException("Authentication failed: No access token found in response.");
            }
        } catch (HttpClientErrorException e) {
            log.debug("Error during user authentication: {}", e.getResponseBodyAsString());
            throw new AuthenticationFailedException("Authentication failed: " + e.getMessage());
        }
    }



    @Override
    public boolean validateToken(String token) throws TokenValidationException {
        String url = "https://" + domain + "/userinfo";
        try {
            String result = new RestTemplate().getForObject(url + "?access_token=" + token, String.class);
            return result != null && !result.isEmpty();
        } catch (Exception e) {
            throw new TokenValidationException("Token validation failed: " + e.getMessage());
        }
    }

    private String getManagementApiToken() {
        String url = "https://" + domain + "/oauth/token";

        Map<String, String> body = new HashMap<>();
        body.put("grant_type", "client_credentials");
        body.put("client_id", managementApiClientId);
        body.put("client_secret", managementApiClientSecret);
        body.put("audience", "https://" + domain + "/api/v2/");

        Map<String, Object> response = new RestTemplate().postForObject(url, body, Map.class);
        return (String) response.get("access_token");
    }

}
