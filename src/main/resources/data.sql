INSERT INTO roles VALUES (1, 'USER') ON CONFLICT DO NOTHING;
INSERT INTO roles VALUES (2, 'ADMIN') ON CONFLICT DO NOTHING;

INSERT INTO users (id, email, login, "password") VALUES(1, 'admin@gmail.com', 'admin', '$2a$10$AuEraDSVYWaogzUQYotEbuToe4Qt6N.aw0qBx.5fqiSXAbhcUkdyW') ON CONFLICT DO NOTHING;
INSERT INTO users (id, email, login, "password") VALUES(2, 'user@gmail.com', 'user', '$2a$10$AuEraDSVYWaogzUQYotEbuToe4Qt6N.aw0qBx.5fqiSXAbhcUkdyW') ON CONFLICT DO NOTHING;

--INSERT INTO users_roles (user_id, roles_id) VALUES(1, 2) ON CONFLICT DO NOTHING;
--INSERT INTO users_roles (user_id, roles_id) VALUES(2, 1) ON CONFLICT DO NOTHING;