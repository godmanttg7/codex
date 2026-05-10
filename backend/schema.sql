CREATE DATABASE IF NOT EXISTS toilet_manage DEFAULT CHARACTER SET utf8mb4;
USE toilet_manage;

CREATE TABLE IF NOT EXISTS toilet_feedback (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    toilet_name VARCHAR(100) NOT NULL,
    area VARCHAR(100) NOT NULL,
    cleanliness_score TINYINT NOT NULL,
    queue_score TINYINT NOT NULL,
    odor_score TINYINT NOT NULL,
    comment VARCHAR(500),
    visited_at DATETIME NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);
