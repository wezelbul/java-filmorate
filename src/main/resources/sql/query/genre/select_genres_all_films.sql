SELECT films.film_id, genres.*
FROM films
LEFT JOIN film_genres ON films.film_id = film_genres.film_id
LEFT JOIN genres ON  film_genres.genre_id = genres.genre_id
WHERE film_genres.genre_id IS NOT NULL
ORDER BY films.film_id
