-- Create notifications table (log of all notification attempts)
CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    channel_id BIGINT NOT NULL,
    message TEXT NOT NULL,
    sent_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (channel_id) REFERENCES channels(id),
    INDEX idx_notifications_user_id (user_id),
    INDEX idx_notifications_channel_id (channel_id),
    INDEX idx_notifications_sent_at (sent_at)
);