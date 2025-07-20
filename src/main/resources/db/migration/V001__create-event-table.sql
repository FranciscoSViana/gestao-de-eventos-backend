CREATE TABLE event (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100),
    description VARCHAR(1000),
    event_time TIMESTAMP,
    event_local VARCHAR(200),
    soft_delete BOOLEAN DEFAULT FALSE
);
