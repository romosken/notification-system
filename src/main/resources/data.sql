-- Insert seed data for users
INSERT INTO users (id, name, email, phone_number) VALUES
                                                      (1, 'Alice Johnson', 'alice@example.com', '+1111111111'),
                                                      (2, 'Bob Smith', 'bob@example.com', '+2222222222'),
                                                      (3, 'Charlie Brown', 'charlie@example.com', '+3333333333'),
                                                      (4, 'Diana Prince', 'diana@example.com', '+4444444444'),
                                                      (5, 'Eve Wilson', 'eve@example.com', '+5555555555')
    ON DUPLICATE KEY UPDATE name=VALUES(name), email=VALUES(email), phone_number=VALUES(phone_number);

-- Insert user categories
INSERT INTO user_categories (user_id, category) VALUES
                                                    (1, 'SPORTS'),
                                                    (1, 'FINANCE'),
                                                    (2, 'MOVIES'),
                                                    (3, 'SPORTS'),
                                                    (4, 'FINANCE'),
                                                    (4, 'MOVIES'),
                                                    (5, 'SPORTS'),
                                                    (5, 'FINANCE'),
                                                    (5, 'MOVIES')
    ON DUPLICATE KEY UPDATE user_id=VALUES(user_id), category=VALUES(category);

-- Insert user channels
INSERT INTO user_channels (user_id, channel) VALUES
                                                 (1, 'SMS'),
                                                 (1, 'EMAIL'),
                                                 (2, 'EMAIL'),
                                                 (2, 'PUSH'),
                                                 (3, 'SMS'),
                                                 (4, 'SMS'),
                                                 (4, 'EMAIL'),
                                                 (4, 'PUSH'),
                                                 (5, 'PUSH')
    ON DUPLICATE KEY UPDATE user_id=VALUES(user_id), channel=VALUES(channel);

