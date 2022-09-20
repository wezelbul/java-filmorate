SELECT DISTINCT film_id
FROM film_likes
GROUP BY film_id
ORDER BY COUNT(user_id) DESC, film_id
LIMIT ?