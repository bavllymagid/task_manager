package com.tasks.task_management.security;

import com.tasks.task_management.local.StaticObjects.RolesConst;
import com.tasks.task_management.security.authenticationProvider.CustomAuthenticationFilter;
import com.tasks.task_management.security.authenticationProvider.CustomAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(HttpMethod.POST, "/task/recieve_instance").permitAll()
                                .requestMatchers(HttpMethod.GET, "/task/validate").permitAll()
                                .anyRequest().authenticated()
                )
                .authenticationProvider(new CustomAuthenticationProvider())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

}
