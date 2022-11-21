INSERT INTO roles (id, name)
VALUES (101, 'USER');

INSERT INTO users(id, email, password, first_name, last_name, role_id)
VALUES (101, 'test', '$2a$12$fkZ3889PPR1BP7m45Aiv0OOkFQhoh675nRPGOg6O0HCa/QGSew38K', 'firstName', 'lastName', 101);

INSERT INTO posts (id, text, title, user_id)
VALUES (101, 'test text', 'my custom title', 101);

INSERT INTO comments (id, text, post_id, user_id)
VALUES (101, 'my test comment', 101, 101);