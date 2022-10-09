SELECT review_id, content, is_positive, user_id, film_id, useful
FROM reviews
WHERE review_id = ?