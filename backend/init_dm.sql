-- 达梦 DM8 初始化脚本：不写死自增 ID，所有外键通过业务编码/名称查询获得。
-- 建议使用 SYSDBA 或具有建表权限的账号执行；如需指定模式，请先修改下一行。
SET SCHEMA SYSDBA;

CREATE TABLE tb_toilet (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    toilet_code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(200) NOT NULL,
    district VARCHAR(50),
    open_time VARCHAR(50),
    created_at DATETIME DEFAULT SYSDATE NOT NULL
);

CREATE TABLE toilet_feedback (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    toilet_name VARCHAR(100) NOT NULL,
    area VARCHAR(100) NOT NULL,
    cleanliness_score INT NOT NULL,
    queue_score INT NOT NULL,
    odor_score INT NOT NULL,
    comment VARCHAR(500),
    visited_at DATETIME NOT NULL,
    created_at DATETIME DEFAULT SYSDATE NOT NULL
);

CREATE TABLE tb_facility (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    toilet_id BIGINT NOT NULL,
    facility_name VARCHAR(100) NOT NULL,
    facility_type VARCHAR(50) NOT NULL,
    status VARCHAR(20) DEFAULT 'NORMAL' NOT NULL,
    created_at DATETIME DEFAULT SYSDATE NOT NULL,
    updated_at DATETIME DEFAULT SYSDATE NOT NULL,
    CONSTRAINT fk_facility_toilet FOREIGN KEY (toilet_id) REFERENCES tb_toilet(id)
);

CREATE TABLE tb_repair_order (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    order_no VARCHAR(40) NOT NULL UNIQUE,
    toilet_id BIGINT NOT NULL,
    facility_id BIGINT,
    fault_desc VARCHAR(500) NOT NULL,
    reporter VARCHAR(50) NOT NULL,
    reporter_name VARCHAR(50),
    assignee_name VARCHAR(50),
    status VARCHAR(20) DEFAULT 'PENDING' NOT NULL,
    created_at DATETIME DEFAULT SYSDATE NOT NULL,
    CONSTRAINT fk_repair_toilet FOREIGN KEY (toilet_id) REFERENCES tb_toilet(id),
    CONSTRAINT fk_repair_facility FOREIGN KEY (facility_id) REFERENCES tb_facility(id)
);

CREATE TABLE tb_consumable_stock (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    toilet_id BIGINT NOT NULL,
    consumable_name VARCHAR(100) NOT NULL,
    current_stock INT DEFAULT 0 NOT NULL,
    min_stock INT DEFAULT 10 NOT NULL,
    CONSTRAINT fk_stock_toilet FOREIGN KEY (toilet_id) REFERENCES tb_toilet(id)
);

CREATE TABLE tb_message (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    receiver_name VARCHAR(50) NOT NULL,
    title VARCHAR(100) NOT NULL,
    content VARCHAR(500) NOT NULL,
    read_flag INT DEFAULT 0 NOT NULL,
    create_time DATETIME DEFAULT SYSDATE NOT NULL,
    update_time DATETIME DEFAULT SYSDATE NOT NULL
);

INSERT INTO tb_toilet (toilet_code, name, address, district, open_time)
SELECT 'T-001', '人民公园东门公厕', '人民公园东门旁', '中心区', '06:00-22:00'
FROM dual WHERE NOT EXISTS (SELECT 1 FROM tb_toilet WHERE toilet_code = 'T-001');

INSERT INTO tb_toilet (toilet_code, name, address, district, open_time)
SELECT 'T-002', '政务中心一层公厕', '政务中心一层大厅西侧', '政务区', '全天开放'
FROM dual WHERE NOT EXISTS (SELECT 1 FROM tb_toilet WHERE toilet_code = 'T-002');

INSERT INTO tb_facility (toilet_id, facility_name, facility_type, status)
SELECT t.id, '一号厕位门锁', '厕位设施', 'NORMAL'
FROM tb_toilet t
WHERE t.toilet_code = 'T-001'
  AND NOT EXISTS (SELECT 1 FROM tb_facility f WHERE f.toilet_id = t.id AND f.facility_name = '一号厕位门锁');

INSERT INTO tb_facility (toilet_id, facility_name, facility_type, status)
SELECT t.id, '洗手液机', '耗材设备', 'NORMAL'
FROM tb_toilet t
WHERE t.toilet_code = 'T-002'
  AND NOT EXISTS (SELECT 1 FROM tb_facility f WHERE f.toilet_id = t.id AND f.facility_name = '洗手液机');

INSERT INTO tb_consumable_stock (toilet_id, consumable_name, current_stock, min_stock)
SELECT t.id, '厕纸', 6, 10
FROM tb_toilet t
WHERE t.toilet_code = 'T-001'
  AND NOT EXISTS (SELECT 1 FROM tb_consumable_stock s WHERE s.toilet_id = t.id AND s.consumable_name = '厕纸');

INSERT INTO tb_consumable_stock (toilet_id, consumable_name, current_stock, min_stock)
SELECT t.id, '洗手液', 20, 8
FROM tb_toilet t
WHERE t.toilet_code = 'T-002'
  AND NOT EXISTS (SELECT 1 FROM tb_consumable_stock s WHERE s.toilet_id = t.id AND s.consumable_name = '洗手液');
