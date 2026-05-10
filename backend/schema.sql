CREATE DATABASE IF NOT EXISTS toilet_manage DEFAULT CHARACTER SET utf8mb4;
USE toilet_manage;

CREATE TABLE IF NOT EXISTS tb_toilet (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    toilet_code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(200) NOT NULL,
    district VARCHAR(50),
    open_time VARCHAR(50),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

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

CREATE TABLE IF NOT EXISTS tb_repair_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    toilet_id BIGINT NOT NULL,
    fault_desc VARCHAR(500) NOT NULL,
    reporter VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_repair_toilet FOREIGN KEY (toilet_id) REFERENCES tb_toilet(id)
);

CREATE TABLE IF NOT EXISTS tb_consumable_stock (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    toilet_id BIGINT NOT NULL,
    consumable_name VARCHAR(100) NOT NULL,
    current_stock INT NOT NULL DEFAULT 0,
    min_stock INT NOT NULL DEFAULT 10,
    CONSTRAINT fk_stock_toilet FOREIGN KEY (toilet_id) REFERENCES tb_toilet(id)
);
