-- Seed data for notification system
-- This file contains base data for all tables
-- Insert Categories
INSERT INTO categories (id, name)
VALUES (1, 'Sports'),
       (2, 'Finance'),
       (3, 'Movies');
-- Insert Channels
INSERT INTO channels (id, name)
VALUES (1, 'EMAIL'),
       (2, 'PUSH'),
       (3, 'SMS');
-- Insert Users
INSERT INTO users (id, name, email, phone_number)
VALUES (1,
        'Alice Johnson',
        'alice@example.com',
        '+1111111111'),
       (2, 'Bob Smith', 'bob@example.com', '+2222222222'),
       (3,
        'Charlie Brown',
        'charlie@example.com',
        '+3333333333'),
       (4,
        'Diana Prince',
        'diana@example.com',
        '+4444444444'),
       (5,
        'Eve Wilson',
        'eve@example.com',
        '+5555555555');
-- Insert User-Category relationships (users_categories)
INSERT INTO users_categories (user_id, category_id)
VALUES -- Alice subscribes to Sports and Finance
       (1, 1),
       -- Sports
       (1, 2),
       -- Finance
       -- Bob subscribes to Movies
       (2, 3),
       -- Movies
       -- Charlie subscribes to Sports
       (3, 1),
       -- Sports
       -- Diana subscribes to Finance and Movies
       (4, 2),
       -- Finance
       (4, 3),
       -- Movies
       -- Eve subscribes to all categories
       (5, 1),
       -- Sports
       (5, 2),
       -- Finance
       (5, 3);
-- Insert User-Channel relationships (users_channels)
INSERT INTO users_channels (user_id, channel_id)
VALUES -- Alice uses SMS and Email
       (1, 3),
       -- SMS
       (1, 1),
       -- EMAIL
       -- Bob uses Email and Push
       (2, 1),
       -- EMAIL
       (2, 2),
       -- PUSH
       -- Charlie uses SMS
       (3, 3),
       -- SMS
       -- Diana uses all channels
       (4, 1),
       -- EMAIL
       (4, 2),
       -- PUSH
       (4, 3),
       -- SMS
       -- Eve uses Push
       (5, 2);