package com.tasks.task_management.security.authenticationProvider;

import com.tasks.task_management.kafka.payload.ValidateUserPayload;
import com.tasks.task_management.kafka.utils.PendingRequestManager;
import com.tasks.task_management.local.StaticObjects.UserSingleton;
import com.tasks.task_management.local.exceptions.InvalidTokenException;
import com.tasks.task_management.remote.utils.payload.UserInstance;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TaskAuthenticationFilter extends OncePerRequestFilter {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final PendingRequestManager pendingRequestManager;

    @Autowired
    public TaskAuthenticationFilter(KafkaTemplate<String, Object> kafkaTemplate,
                                    PendingRequestManager pendingRequestManager) {
        this.kafkaTemplate = kafkaTemplate;
        this.pendingRequestManager = pendingRequestManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            UserSingleton user;
            String token = authorizationHeader.substring(7);
            String correlationId = UUID.randomUUID().toString();
            pendingRequestManager.createRequest(correlationId);
            kafkaTemplate.send("validateUser", new ValidateUserPayload(correlationId, token));
            try {
                UserInstance userIn = pendingRequestManager.getRequest(correlationId).get(5, TimeUnit.SECONDS);
                UserSingleton.setInstance(userIn);
                user = UserSingleton.getInstance();
                if (user.getEmail() != null) {
                    UsernamePasswordAuthenticationToken authentication = getUsernamePasswordAuthenticationToken(user);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (InvalidTokenException ex) {
                request.setAttribute("exception status", HttpServletResponse.SC_UNAUTHORIZED);
                request.setAttribute("exception message", "Invalid token");
            }catch (TimeoutException ex) {
                request.setAttribute("exception status", HttpServletResponse.SC_REQUEST_TIMEOUT);
                request.setAttribute("exception message", "Request timeout");
            }
            catch (Exception ex) {
                request.setAttribute("exception status", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                request.setAttribute("exception message", "Server error");
            }
        }
        filterChain.doFilter(request, response);
    }

    private static UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(UserSingleton user) {
        List<String> roles = user.getRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_" + role);
            authorities.add(simpleGrantedAuthority);
        }
        return new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);
    }
}
