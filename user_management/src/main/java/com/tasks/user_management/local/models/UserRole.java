package com.tasks.user_management.local.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;


@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private BigInteger id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public UserRole(String name) {
        this.name = name;
    }
}
