package com.tasks.task_management.security;

import com.tasks.task_management.local.StaticObjects.RolesConst;
import com.tasks.task_management.security.authenticationProvider.CustomAuthEntryPoint;
import com.tasks.task_management.security.authenticationProvider.CustomAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableScheduling
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationConfiguration authConfig) throws Exception {
        AuthenticationManager authManager = authConfig.getAuthenticationManager();

        http.csrf().disable()
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(HttpMethod.POST, "/api/task/receive_instance").permitAll()
                                .requestMatchers(HttpMethod.DELETE,"/api/task/delete/**").hasRole(RolesConst.USER.name())
                                .requestMatchers(HttpMethod.PUT,"/api/task/update").hasRole(RolesConst.USER.name())
                                .requestMatchers(HttpMethod.POST,"/api/task/create").hasRole(RolesConst.USER.name())
                                .requestMatchers(HttpMethod.GET,"/api/task/get/user_tasks/**").hasRole(RolesConst.USER.name())
                                .requestMatchers(HttpMethod.GET,"/api/task/get/**").hasRole(RolesConst.USER.name())
                                .requestMatchers(HttpMethod.POST,"/api/task/assign/**").hasRole(RolesConst.USER.name())
                                .requestMatchers(HttpMethod.GET,"/api/task/get/user_assigned_tasks/**").hasRole(RolesConst.USER.name())
                                .requestMatchers("/api/task/unassign/**").hasRole(RolesConst.USER.name())
                                .anyRequest().authenticated()
                )
                .addFilterBefore(new CustomAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }

}
