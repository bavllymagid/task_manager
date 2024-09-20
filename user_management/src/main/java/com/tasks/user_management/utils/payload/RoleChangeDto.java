package com.tasks.user_management.utils.payload;

import com.tasks.user_management.utils.validators.ValidRole;
import lombok.Data;

@Data
public class RoleChangeDto {
    @ValidRole(message = "Role must be either 'ADMIN' or 'USER' or 'MANAGER'")
    String role;
}
