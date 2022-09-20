SELECT users.*
FROM users
INNER JOIN user_friends ON users.user_id = user_friends.friend_id
WHERE user_friends.user_id = ?