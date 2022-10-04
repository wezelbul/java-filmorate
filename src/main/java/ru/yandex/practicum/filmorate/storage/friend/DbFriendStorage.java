package ru.yandex.practicum.filmorate.storage.friend;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;
import ru.yandex.practicum.filmorate.util.UtilReader;

import java.util.List;
import java.util.Objects;

@Repository
public class DbFriendStorage implements FriendStorage {

    private final JdbcTemplate userFriends;

    private static final String SQL_QUERY_DIR = "src/main/resources/sql/query/user/friend/";
    private static final String SELECT_BY_ID_SQL_QUERY = UtilReader.readString(
            SQL_QUERY_DIR + "select_by_id.sql");
    private static final String SELECT_ALL_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "select_all.sql");
    private static final String SELECT_COMMON_SQL_QUERY = UtilReader.readString(
            SQL_QUERY_DIR + "select_common.sql");
    private static final String SELECT_COUNT_LIMIT_SQL_QUERY = UtilReader.readString(
            SQL_QUERY_DIR + "select_count_limit.sql");
    private static final String SELECT_CONFIRMING_STATUS_SQL_QUERY = UtilReader.readString(
            SQL_QUERY_DIR + "select_confirming_status.sql");
    private static final String INSERT_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "insert.sql");
    private static final String DELETE_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "delete.sql");
    private static final String UPDATE_CONFIRMING_STATUS_SQL_QUERY = UtilReader.readString(
            SQL_QUERY_DIR + "update_confirming_status.sql");
    private static final String DELETE_ALL_FRIENDS_OF_USER_SQL_QUERY = UtilReader.readString(
            SQL_QUERY_DIR + "delete_all_friends_of_user.sql");
    private static final String DELETE_USER_FROM_ALL_FRIENDS_QUERY = UtilReader.readString(
            SQL_QUERY_DIR + "delete_user_from_all_friends.sql");

    public DbFriendStorage(JdbcTemplate userFriends) {
        this.userFriends = userFriends;
    }

    @Override
    public List<Long> getValue(Long userId) {
        return userFriends.queryForList(SELECT_BY_ID_SQL_QUERY, Long.class, userId);
    }

    @Override
    public boolean addLink(Long userId, Long friendId) throws DataIntegrityViolationException {
        userFriends.update(INSERT_SQL_QUERY, userId, friendId);
        return true;
    }

    @Override
    public boolean deleteLink(Long userId, Long friendId) {
        userFriends.update(DELETE_SQL_QUERY, userId, friendId);
        return true;
    }

    @Override
    public List<Long> getMostPopularObjectId(Integer count) {
        return userFriends.queryForList(SELECT_COUNT_LIMIT_SQL_QUERY, Long.class, count);
    }

    @Override
    public Boolean getConfirmingStatus(Long userId, Long friendId) {
        return userFriends.query(SELECT_CONFIRMING_STATUS_SQL_QUERY,
                (rs, rowNum) -> rs.getObject("confirming_status", Boolean.class), userId, friendId, friendId, userId)
                .stream().anyMatch(Objects::nonNull);
    }

    @Override
    public List<User> getFriends(Long userId) {
        return userFriends.query(SELECT_ALL_SQL_QUERY, new UserMapper(), userId);
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long friendId) {
        return userFriends.query(SELECT_COMMON_SQL_QUERY, new UserMapper(), userId, friendId);
    }

    @Override
    public Boolean deleteAllFriendsOfUser(Long userId) {
        userFriends.update(DELETE_ALL_FRIENDS_OF_USER_SQL_QUERY, userId);
        userFriends.update(DELETE_USER_FROM_ALL_FRIENDS_QUERY, userId);
        return true;
    }
}
