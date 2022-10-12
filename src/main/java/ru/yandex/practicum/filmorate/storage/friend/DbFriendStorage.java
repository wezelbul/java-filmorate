package ru.yandex.practicum.filmorate.storage.friend;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.base.DataObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;

import static ru.yandex.practicum.filmorate.storage.friend.FriendRequests.*;

import java.util.List;
import java.util.Objects;

@Repository
public class DbFriendStorage implements FriendStorage {

    private final JdbcTemplate userFriends;
    private final UserMapper userMapper = new UserMapper();

    public DbFriendStorage(JdbcTemplate userFriends) {
        this.userFriends = userFriends;
    }

    @Override
    public List<Long> getValue(Long userId) {
        return userFriends.queryForList(SELECT_BY_ID.getSqlQuery(), Long.class, userId);
    }

    @Override
    public boolean addLink(Long userId, Long friendId) throws DataIntegrityViolationException {
        userFriends.update(INSERT.getSqlQuery(), userId, friendId);
        return true;
    }

    @Override
    public boolean deleteLink(Long userId, Long friendId) {
        userFriends.update(DELETE.getSqlQuery(), userId, friendId);
        return true;
    }

    @Override
    public List<Long> getMostPopularObjectId(Integer count) {
        return userFriends.queryForList(SELECT_COUNT_LIMIT.getSqlQuery(), Long.class, count);
    }

    @Override
    public Boolean getConfirmingStatus(Long userId, Long friendId) {
        return userFriends.query(SELECT_CONFIRMING_STATUS.getSqlQuery(),
                (rs, rowNum) -> rs.getObject("confirming_status", Boolean.class), userId, friendId, friendId, userId)
                .stream().anyMatch(Objects::nonNull);
    }

    @Override
    public List<User> getFriends(Long userId) {
        User user = userFriends.query(SELECT_USER_BY_ID.getSqlQuery(), userMapper, userId)
                .stream()
                .findAny()
                .orElse(null);
        if(user != null){
            return userFriends.query(SELECT_ALL.getSqlQuery(), userMapper, userId);
        }
        throw new DataObjectNotFoundException(userId);
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long friendId) {
        return userFriends.query(SELECT_COMMON.getSqlQuery(), userMapper, userId, friendId);
    }

    @Override
    public Boolean deleteAllFriendsOfUser(Long userId) {
        userFriends.update(DELETE_ALL_FRIENDS_OF_USER.getSqlQuery(), userId);
        userFriends.update(DELETE_USER_FROM_ALL_FRIENDS.getSqlQuery(), userId);
        return true;
    }
}
