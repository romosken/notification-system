-- Create users_channels junction table (ManyToMany relationship)
CREATE TABLE IF NOT EXISTS users_channels (
    user_id BIGINT NOT NULL,
    channel_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, channel_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (channel_id) REFERENCES channels(id) ON DELETE CASCADE,
    INDEX idx_users_channels_user (user_id)
);