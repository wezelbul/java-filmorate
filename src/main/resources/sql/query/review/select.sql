SELECT review_id, content, is_positive, user_id, film_id, useful
FROM reviews
ORDER BY useful DESC LIMIT ?