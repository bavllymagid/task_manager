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

public class TaskAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            UserSingleton user;
            String token = authorizationHeader.substring(7);
            try {
                Requests.validateToken(token);
                user = UserSingleton.getInstance();
                if (user.getEmail() != null) {
                    UsernamePasswordAuthenticationToken authentication = getUsernamePasswordAuthenticationToken(user);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (InvalidTokenException ex) {
                request.setAttribute("exception status", HttpServletResponse.SC_UNAUTHORIZED);
                request.setAttribute("exception message", "Invalid token");
            }catch (Exception ex) {
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
