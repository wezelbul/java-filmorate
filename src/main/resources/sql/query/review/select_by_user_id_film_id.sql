SELECT review_id, content, is_positive, user_id, film_id, useful
FROM reviews
WHERE user_id = ? AND film_id = ?