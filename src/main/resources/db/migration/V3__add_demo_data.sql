INSERT INTO app_user(id, username, password) VALUES (1, 'user', '$2a$10$/isY34asNF7m6SQ2DHfHYeWeckxvXScbgtGoXo2CAJqpiAFDAXyl2');
INSERT INTO app_user(id, username, password) VALUES (2, 'user_2', '$2a$10$/isY34asNF7m6SQ2DHfHYeWeckxvXScbgtGoXo2CAJqpiAFDAXyl2');

INSERT INTO category(id, name) VALUES (1, 'Category 1');
INSERT INTO category(id, name) VALUES (2, 'Category 2');

INSERT INTO product(id, user_id, category_id, name, description, status, created_at) VALUES (1, 1, 1, 'Product 1', '', 'HIDDEN', NOW());
INSERT INTO product(id, user_id, category_id, name, description, status, created_at) VALUES (2, 1, null, 'Product 2', '', 'ON_REVIEW', NOW());
INSERT INTO product(id, user_id, category_id, name, description, status, created_at) VALUES (3, 2, null, 'Product 3', '', 'HIDDEN', NOW());

select setval(pg_get_serial_sequence('product', 'id'), (select count(*) from product));
select setval(pg_get_serial_sequence('category', 'id'), (select count(*) from category));
select setval(pg_get_serial_sequence('app_user', 'id'), (select count(*) from app_user));
select setval(pg_get_serial_sequence('product', 'id'), (select count(*) from product));
