-- drop user_roles table --
DROP TABLE IF EXISTS user_roles; 
-- Drop roles table if needed --
DROP TABLE IF EXISTS roles;
-- drop refresh_tokens table --
DROP TABLE IF EXISTS refresh_tokens; 
-- drop task_assignments table --
DROP TABLE IF EXISTS task_assignments; 
-- drop notifications table --
DROP TABLE IF EXISTS notifications; 
-- drop tasks table --
DROP TABLE IF EXISTS tasks; 
-- drop users table --
DROP TABLE IF EXISTS users;
-- drop groups table --
DROP TABLE IF EXISTS groups;
-- drop user_groups table --
DROP TABLE IF EXISTS user_groups;


-- ====---------------- Authentication Service ----------------==== --

-- create users table --
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    secret_token VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_users_email ON users(email);

-- create group table --
CREATE TABLE groups (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL UNIQUE
);

-- create user_groups table --
CREATE TABLE user_groups (
    group_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, group_id),
    FOREIGN KEY (group_id) REFERENCES groups(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- create roles table --
CREATE TABLE roles (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL UNIQUE
);
-- insert roles in the begining-- 
insert into roles values(1, "USER");
insert into roles values(2, "ADMIN");
insert into roles values(3, "MANAGER");

-- Create user_roles table --
CREATE TABLE user_roles (
	role_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
CREATE INDEX idx_user_roles_user_id ON user_roles(user_id);
CREATE INDEX idx_role_roles_user_id ON user_roles(role_id);


-- create refresh_tokens table --
CREATE TABLE refresh_tokens (
    token_id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    refresh_token VARCHAR(255) NOT NULL UNIQUE,
    secret_refresh VARCHAR(255) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens(user_id);
CREATE INDEX idx_refresh_tokens_token ON refresh_tokens(refresh_token);

-- ====---------------- Authentication Service ----------------==== --

-- ====---------------- Tasks Service ----------------==== --

-- create tasks table --
CREATE TABLE tasks (
    task_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    `description` TEXT,
    `status` VARCHAR(50) NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    due_date TIMESTAMP
);
CREATE INDEX idx_tasks_created_by ON tasks(user_id);
CREATE INDEX idx_tasks_status ON tasks(`status`);

-- create task_assignments table --
CREATE TABLE task_assignments (
    assignment_id SERIAL PRIMARY KEY,
    task_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    assigned_by BIGINT NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES tasks(task_id) ON DELETE CASCADE
);
CREATE INDEX idx_task_assignments_task_id ON task_assignments(task_id);
CREATE INDEX idx_task_assignments_user_id ON task_assignments(user_id);
CREATE INDEX idx_task_assignments_assigned_by ON task_assignments(assigned_by);

-- create notifications table --
CREATE TABLE notifications (
    notification_id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    task_id BIGINT NOT NULL,
    `type` VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    `read` BOOLEAN DEFAULT FALSE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_notifications_user_id ON notifications(user_id);
CREATE INDEX idx_notifications_task_id ON notifications(task_id);
CREATE INDEX idx_notifications_read ON notifications(`read`);
CREATE INDEX idx_notifications_type ON notifications(`type`);

-- ====---------------- Tasks Service ----------------==== --