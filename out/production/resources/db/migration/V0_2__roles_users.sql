INSERT INTO user_acc (username, password, first_name, last_name, age, phone, email) VALUES ('admin', '$2a$10$niOaiTgHZHhLEkMxJwHIVuHowzSbNnocB87uA/p7dSZFG55y6CR8.',
'Admin', 'Kasutaja', 21, 57464847, 'heiko.piirme@khk.ee');

INSERT INTO user_acc (username, password, first_name, last_name, age, phone, email) VALUES ('moderaator', '$2a$10$niOaiTgHZHhLEkMxJwHIVuHowzSbNnocB87uA/p7dSZFG55y6CR8.',
'Moderaator', 'Kasutaja', 21, 57464847, 'heiko.piirme@khk.ee');

INSERT INTO user_acc (username, password, first_name, last_name, age, phone, email) VALUES ('tavakasutaja', '$2a$10$niOaiTgHZHhLEkMxJwHIVuHowzSbNnocB87uA/p7dSZFG55y6CR8.',
'Tava', 'Kasutaja', 21, 57464847, 'heiko.piirme@khk.ee');


INSERT INTO user_has_role (user_id, role_id) VALUES (1, 3);
INSERT INTO user_has_role (user_id, role_id) VALUES (2, 2);
INSERT INTO user_has_role (user_id, role_id) VALUES (3, 1);