package com.tasks.user_management.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "user_roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {
    @EmbeddedId
    private UserRoleId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User user;

    @Column(name = "role", insertable = false, updatable = false)
    private String role;
}
