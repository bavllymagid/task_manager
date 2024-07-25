package com.tasks.user_management.security.jwt;

import com.tasks.user_management.local.models.User;
import com.tasks.user_management.local.models.UserRole;
import com.tasks.user_management.services.RefreshTokenService;
import com.tasks.user_management.utils.exceptions.UserNotFound;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public JwtAuthenticationFilter(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            User user = null;
            try {
                user = refreshTokenService.getUserFromToken(token);
            } catch (UserNotFound e) {
                throw new RuntimeException(e);
            }
            if (user.getEmail() != null) {
                UsernamePasswordAuthenticationToken authentication = getUsernamePasswordAuthenticationToken(user);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    private static UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(User user) {
        List<UserRole> roles = user.getUserRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (UserRole role : roles) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_" + role.getRole());
            authorities.add(simpleGrantedAuthority);
        }
        return new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);
    }

}
