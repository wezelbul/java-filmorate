SET MODE MySQL;

CREATE TABLE IF NOT EXISTS genres
(
    genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genre_name VARCHAR(200) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS mpa_rating
(
    mpa_rating_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    mpa_name VARCHAR(200) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS films
(
    film_id LONG GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(200),
    release_date DATE,
    duration_in_minutes INTEGER,
    mpa_rating_id INTEGER
);

CREATE TABLE IF NOT EXISTS users
(
    user_id LONG GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email VARCHAR(300) NOT NULL UNIQUE,
    login VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(150),
    birthday DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS film_likes
(
    film_id LONG NOT NULL REFERENCES films (film_id),
    user_id LONG NOT NULL REFERENCES users (user_id),
    PRIMARY KEY (film_id, user_id)
);

CREATE TABLE IF NOT EXISTS user_friends
(
    user_id LONG NOT NULL REFERENCES users (user_id),
    friend_id LONG NOT NULL REFERENCES users (user_id),
    PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS film_genres
(
    film_id LONG NOT NULL REFERENCES films (film_id),
    genre_id INTEGER NOT NULL REFERENCES genres (genre_id),
    PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS reviews
(
    review_id   LONG GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    content     VARCHAR,
    is_positive BOOLEAN, -- если false то отризательный, если true то положительный
    user_id     LONG NOT NULL REFERENCES users (user_id),
    film_id     LONG NOT NULL REFERENCES films (film_id),
    useful      INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (film_id) REFERENCES films (film_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS reviews_likes
(
    review_id   LONG,
    user_id     LONG,
    is_like     BOOLEAN, -- если false то дизлайк, если true то лайк
    PRIMARY KEY (review_id, user_id),
    FOREIGN KEY (review_id) REFERENCES reviews (review_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);