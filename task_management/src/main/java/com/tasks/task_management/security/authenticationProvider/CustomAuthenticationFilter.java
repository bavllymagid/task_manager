package com.tasks.task_management.security.authenticationProvider;

import com.tasks.task_management.local.StaticObjects.UserSingleton;
import com.tasks.task_management.local.exceptions.InvalidTokenException;
import com.tasks.task_management.remote.utils.requests.Requests;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            UserSingleton user;
            String token = authorizationHeader.substring(7);
            Requests.validateToken(token);
            try {
                user = UserSingleton.getInstance();
                if (user.getEmail() != null) {
                    UsernamePasswordAuthenticationToken authentication = getUsernamePasswordAuthenticationToken(user);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (InvalidTokenException ex) {
                // Log or handle token validation errors
                System.out.println("Invalid token exception: " + ex.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
            }catch (Exception ex) {
                // Log or handle generic exceptions
                System.out.println("Exception in filter: " + ex.getMessage());
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
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
