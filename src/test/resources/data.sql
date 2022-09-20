INSERT INTO films (name, description, release_date, duration_in_minutes, mpa_rating_id)
VALUES ('Бриллиантовая рука',
        'Нетленка Гайдая про зарубежную травматологию',
        '1969-04-28',
        100,
        3),
       ('Властелин колец: Братство Кольца',
        'Галадриэль с Кольцом Всевластия снится мне в кошмарах',
        '2001-12-13',
        178,
        3),
       ('Криминальное чтиво',
        'Обзорная экскурсия по сетям общественного питания во Франции',
        '1994-05-21',
        154,
        4);

INSERT INTO users (email, login, name, birthday)
VALUES ('traumatology@email.xyz',
        'trauma',
        'Robert',
        '1995-10-02'),
       ('exampla@example.biz',
        'example',
        'EXAMPLE',
        '2002-02-20'),
       ('secksinstructornatto@otan.geo',
        'ganz',
        'Hans',
        '1960-11-12');

INSERT IGNORE INTO film_likes (film_id, user_id)
VALUES (1, 1),
       (2, 1),
       (2, 2),
       (3, 1),
       (3, 2),
       (3, 3);

INSERT IGNORE INTO user_friends (user_id, friend_id)
VALUES (1, 2),
       (2, 1),
       (2, 3),
       (3, 1),
       (3, 2);

INSERT IGNORE INTO film_genres (film_id, genre_id)
VALUES (1, 1),
       (2, 2),
       (3, 1),
       (3, 4);