SELECT DISTINCT user_id
FROM user_friends
GROUP BY user_id
ORDER BY COUNT(friend_id) DESC, user_id
LIMIT ?