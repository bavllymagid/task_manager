-- ====---------------- Authentication Service ----------------==== --

-- drop users table --
DROP TABLE IF EXISTS USERS; 
-- create users table --
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `role` VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_users_email ON users(email);

-- drop refresh_tokens table --
DROP TABLE IF EXISTS refresh_tokens; 
-- create refresh_tokens table --
CREATE TABLE refresh_tokens (
    token_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id),
    refresh_token VARCHAR(255) NOT NULL UNIQUE,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens(user_id);
CREATE INDEX idx_refresh_tokens_expires_at ON refresh_tokens(expires_at);

-- ====---------------- Authentication Service ----------------==== --

-- ====---------------- Tasks Service ----------------==== --

-- drop tasks table --
DROP TABLE IF EXISTS tasks; 
-- create tasks table --
CREATE TABLE tasks (
    task_id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    `description` TEXT,
    `status` VARCHAR(50) NOT NULL,
    created_by INT REFERENCES users(user_id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_tasks_created_by ON tasks(created_by);
CREATE INDEX idx_tasks_status ON tasks(`status`);

-- drop task_assignments table --
DROP TABLE IF EXISTS task_assignments; 
-- create task_assignments table --
CREATE TABLE task_assignments (
    assignment_id SERIAL PRIMARY KEY,
    task_id INT REFERENCES tasks(task_id),
    user_id INT REFERENCES users(user_id),
    assigned_by INT REFERENCES users(user_id),
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_task_assignments_task_id ON task_assignments(task_id);
CREATE INDEX idx_task_assignments_user_id ON task_assignments(user_id);
CREATE INDEX idx_task_assignments_assigned_by ON task_assignments(assigned_by);

-- drop notifications table --
DROP TABLE IF EXISTS notifications; 
-- create notifications table --
CREATE TABLE notifications (
    notification_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id),
    task_id INT REFERENCES tasks(task_id),
    `type` VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    `read` BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_notifications_user_id ON notifications(user_id);
CREATE INDEX idx_notifications_task_id ON notifications(task_id);
CREATE INDEX idx_notifications_read ON notifications(`read`);
CREATE INDEX idx_notifications_type ON notifications(`type`);

-- ====---------------- Tasks Service ----------------==== --