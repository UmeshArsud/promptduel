-- PromptDuel Database Schema (MySQL 8)
-- This file is for reference / production use.
-- In development, Hibernate ddl-auto=update handles schema creation.

CREATE DATABASE IF NOT EXISTS promptduel
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE promptduel;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_users_email (email)
) ENGINE=InnoDB;

-- Projects table
CREATE TABLE IF NOT EXISTS projects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_projects_user_id (user_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Prompt versions table
CREATE TABLE IF NOT EXISTS prompt_versions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id BIGINT NOT NULL,
    version_label VARCHAR(50) NOT NULL,
    system_prompt_text TEXT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_prompt_versions_project_id (project_id),
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Test inputs table
CREATE TABLE IF NOT EXISTS test_inputs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id BIGINT NOT NULL,
    input_text TEXT NOT NULL,
    expected_keywords VARCHAR(1000),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_test_inputs_project_id (project_id),
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Test runs table (evaluation results)
CREATE TABLE IF NOT EXISTS test_runs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    prompt_version_id BIGINT NOT NULL,
    test_input_id BIGINT NOT NULL,
    model_output TEXT,
    score_json TEXT,
    latency_ms BIGINT,
    status VARCHAR(20) NOT NULL DEFAULT 'COMPLETED',
    error_message VARCHAR(1000),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_test_runs_prompt_version_id (prompt_version_id),
    INDEX idx_test_runs_test_input_id (test_input_id),
    FOREIGN KEY (prompt_version_id) REFERENCES prompt_versions(id) ON DELETE CASCADE,
    FOREIGN KEY (test_input_id) REFERENCES test_inputs(id) ON DELETE CASCADE
) ENGINE=InnoDB;
