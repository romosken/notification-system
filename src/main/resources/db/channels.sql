-- Create channels table
CREATE TABLE IF NOT EXISTS channels (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    INDEX idx_channels_name (name)
);

