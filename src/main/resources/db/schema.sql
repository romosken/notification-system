-- Schema file for notification system
-- Generated based on JPA entities

-- Create categories table
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    INDEX idx_categories_name (name)
);

-- Create channels table
CREATE TABLE IF NOT EXISTS channels (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    INDEX idx_channels_name (name)
);

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(50),
    INDEX idx_users_email (email)
);

-- Create users_categories junction table (ManyToMany relationship)
CREATE TABLE IF NOT EXISTS users_categories (
    user_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, category_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE,
    INDEX idx_users_categories_category (category_id),
    INDEX idx_users_categories_user (user_id)
);

-- Create users_channels junction table (ManyToMany relationship)
CREATE TABLE IF NOT EXISTS users_channels (
    user_id BIGINT NOT NULL,
    channel_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, channel_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (channel_id) REFERENCES channels(id) ON DELETE CASCADE,
    INDEX idx_users_channels_user (user_id)
);

-- Create notifications table
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

