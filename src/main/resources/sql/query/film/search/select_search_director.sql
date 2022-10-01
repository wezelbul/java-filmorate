SELECT films.*, mpa_rating.mpa_name, COUNT(film_likes.user_id) AS rate
FROM films
         LEFT JOIN mpa_rating ON films.mpa_rating_id = mpa_rating.mpa_rating_id
         LEFT JOIN film_likes ON films.film_id = film_likes.film_id
WHERE films.director LIKE '%?%'
GROUP BY films.film_id
ORDER BY rate DESC, films.film_id
