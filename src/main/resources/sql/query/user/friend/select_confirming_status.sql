SELECT (
    SELECT user_id
    FROM user_friends
    WHERE user_id = ? AND friend_id = ?
) AND (
    SELECT friend_id
    FROM user_friends
    WHERE user_id = ? AND friend_id = ?
) AS confirming_status