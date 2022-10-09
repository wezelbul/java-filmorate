UPDATE reviews
SET useful = useful - 1
where review_id = ?