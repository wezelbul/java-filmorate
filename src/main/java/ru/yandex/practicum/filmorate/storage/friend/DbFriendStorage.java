package ru.yandex.practicum.filmorate.storage.friend;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;
import ru.yandex.practicum.filmorate.util.UtilReader;

import java.util.List;

@Repository
public class DbFriendStorage implements FriendStorage {

    private final JdbcTemplate userFriends;

    private static final String SQL_QUERY_DIR = "src/main/resources/sql/query/user/friend/";
    private static final String SELECT_BY_ID_SQL_PATH = SQL_QUERY_DIR + "select_by_id.sql";
    private static final String SELECT_ALL_SQL_PATH = SQL_QUERY_DIR + "select_all.sql";
    private static final String SELECT_COMMON_SQL_PATH = SQL_QUERY_DIR + "select_common.sql";
    private static final String SELECT_COUNT_LIMIT_SQL_PATH = SQL_QUERY_DIR + "select_count_limit.sql";
    private static final String SELECT_CONFIRMING_STATUS = SQL_QUERY_DIR + "select_confirming_status.sql";
    private static final String INSERT_SQL_PATH = SQL_QUERY_DIR + "insert.sql";
    private static final String DELETE_SQL_PATH = SQL_QUERY_DIR + "delete.sql";
    private static final String UPDATE_CONFIRMING_STATUS = SQL_QUERY_DIR + "update_confirming_status.sql";

    public DbFriendStorage(JdbcTemplate userFriends) {
        this.userFriends = userFriends;
    }

    @Override
    public List<Long> getValue(Long userId) {
        return userFriends.queryForList(
                UtilReader.readString(SELECT_BY_ID_SQL_PATH), Long.class, userId);
    }

    @Override
    public boolean addLink(Long userId, Long friendId) throws DataIntegrityViolationException {
        userFriends.update(
                UtilReader.readString(INSERT_SQL_PATH), userId, friendId);
        return true;
    }

    @Override
    public boolean deleteLink(Long userId, Long friendId) {
        userFriends.update(
                UtilReader.readString(DELETE_SQL_PATH), userId, friendId);
        return true;
    }

    @Override
    public List<Long> getMostPopularObjectId(Integer count) {
        return userFriends.queryForList(
                UtilReader.readString(SELECT_COUNT_LIMIT_SQL_PATH), Long.class, count);
    }

    @Override
    public Boolean getConfirmingStatus(Long userId, Long friendId) {
        return userFriends.query(UtilReader.readString(SELECT_CONFIRMING_STATUS),
                (rs, rowNum) -> rs.getObject("confirming_status", Boolean.class), userId, friendId)
                .stream().findAny().orElse(false);
    }

    @Override
    public void setConfirmingStatus(boolean status, Long userId, Long friendId) {
        userFriends.update(UtilReader.readString(UPDATE_CONFIRMING_STATUS), status, userId, friendId);
    }

    @Override
    public List<User> getFriends(Long userId) {
        return userFriends.query(UtilReader.readString(SELECT_ALL_SQL_PATH), new UserMapper(), userId);
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long friendId) {
        return userFriends.query(UtilReader.readString(SELECT_COMMON_SQL_PATH), new UserMapper(), userId, friendId);
    }


}
