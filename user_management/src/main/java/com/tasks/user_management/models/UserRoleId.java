package com.tasks.user_management.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigInteger;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleId implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "user_id")
    private BigInteger userId;

    @Column(name = "role")
    private String role;
}