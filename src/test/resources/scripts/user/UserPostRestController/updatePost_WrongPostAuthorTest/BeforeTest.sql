INSERT INTO roles (id, name)
VALUES (101, 'USER');

INSERT INTO users (id, email, first_name, last_name, password, role_id)
VALUES (101, 'user', 'user', 'user', '$2a$12$4u1tSnYIkFdXX1Lh.hEwyOqORMPMI7lLap1.pg.Si62auDld87IaS', 101),
       (102, 'user2', 'user2', 'user2', '$2a$12$4u1tSnYIkFdXX1Lh.hEwyOqORMPMI7lLap1.pg.Si62auDld87IaS', 101);

INSERT INTO posts (id, text, title, user_id)
VALUES (101, 'post text', 'post title', 102);