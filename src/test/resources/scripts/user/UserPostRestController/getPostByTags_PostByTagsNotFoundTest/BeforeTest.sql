INSERT INTO roles (id, name)
VALUES (101, 'USER');

INSERT INTO users(id, email, password, first_name, last_name, role_id)
VALUES (101, 'test', '$2a$12$fkZ3889PPR1BP7m45Aiv0OOkFQhoh675nRPGOg6O0HCa/QGSew38K', 'firstName', 'lastName', 101);

INSERT INTO posts (id, text, title, user_id) VALUES
    (101, 'Do you like twitter? My parents give me some pocket money and i think about this small deal of purchasing that.', 'Do you like twitter?', 101),
    (102, 'Recently i''ve heard smtg like "witch and slave". Seems kinda funny isn''t it ?', 'Wierd  names', 101);

INSERT INTO posts_tags (post_id, tag, id) VALUES
    (101, 'like', 0),
    (101, 'money', 1);