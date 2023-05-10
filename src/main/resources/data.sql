INSERT INTO roles (name)
VALUES ('ROLE_USER'),
       ('ROLE_AGENT'),
       ('ROLE_ADMIN');

INSERT INTO users (email, first_name, last_name, password, phone_number, username)
VALUES ('admin@admin.com', 'admin', 'admin', '$2y$10$vDFwLkud.n7t.1g7WX5Os.c0/8j66cNAvEuM4Ssfi3z5Dn0HzYoua', '+77777777777', 'admin'),
       ('ivan@test.com', 'Иван', 'Иванов', '$2y$10$vDFwLkud.n7t.1g7WX5Os.c0/8j66cNAvEuM4Ssfi3z5Dn0HzYoua', '+77777777777', 'testuser_ivan');

INSERT INTO users_roles (user_id, roles_id)
VALUES (1, 3),
       (2, 1);

INSERT INTO real_estates (address, description, latitude, longitude, name, price, user_id)
VALUES ('улица Льва Толстого, дом 16', 'Красивый и уютный дом в центре Москвы', 55.740114, 37.617388, 'Дом на Льва Толстого', 100000000, 2),
       ('Кутузовский проспект, дом 7', 'Шикарная квартира с видом на Москву-реку', 55.738182, 37.563065, 'Квартира на Кутузовском', 50000000, 2),
       ('Большой Кисельный переулок, дом 6', 'Уютная квартира в историческом центре Москвы', 55.758537, 37.616066, 'Квартира на Кисельном', 30000000, 2),
       ('проспект Мира, дом 119', 'Просторная квартира вблизи станции метро', 55.780334, 37.633685, 'Квартира на Мира', 20000000, 2),
       ('Новый Арбат, дом 24', 'Студия в элитном жилом комплексе', 55.751972, 37.584575, 'Студия на Новом Арбате', 10000000, 2);