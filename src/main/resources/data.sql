INSERT IGNORE INTO mpa_rating (mpa_rating_id, mpa_name)
VALUES (1, 'G'),
       (2, 'PG'),
       (3, 'PG-13'),
       (4, 'R'),
       (5, 'NC-17');

INSERT IGNORE INTO genres (genre_id, genre_name)
VALUES (1, 'Комедия'),
       (2, 'Драма'),
       (3, 'Мультфильм'),
       (4, 'Триллер'),
       (5, 'Документальное'),
       (6, 'Боевик');

INSERT IGNORE INTO event_types (event_type)
VALUES ('LIKE'),
       ('REVIEW'),
       ('FRIEND');

INSERT IGNORE INTO event_operations (event_operation)
VALUES ('REMOVE'),
       ('ADD'),
       ('UPDATE');