-- Create user_channels junction table
CREATE TABLE IF NOT EXISTS user_channels (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    channel VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_channel (user_id, channel),
    INDEX idx_user_id (user_id),
    INDEX idx_channel (channel)
    );

