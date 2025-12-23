-- Create user_categories junction table
CREATE TABLE IF NOT EXISTS user_categories (
    user_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, category_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE,
    INDEX idx_users_categories_category (category_id),
    INDEX idx_users_categories_user (user_id)
);

