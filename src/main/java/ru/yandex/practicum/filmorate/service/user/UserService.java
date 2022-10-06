package ru.yandex.practicum.filmorate.service.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.base.DataObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.base.data.AbstractDataService;
import ru.yandex.practicum.filmorate.storage.friend.DbFriendStorage;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.DbUserStorage;

import java.util.*;

@Service
public class UserService extends AbstractDataService<User, DbUserStorage> {

    private final FriendStorage friendStorage;
    private final LikeStorage likeStorage;
    private final Integer defaultCountPopularUsers = 10;

    public UserService(DbUserStorage userStorage, DbFriendStorage friendStorage,LikeStorage likeStorage) {
        super(userStorage);
        this.friendStorage = friendStorage;
        this.likeStorage=likeStorage;
    }

    @Override
    protected Class<User> getClassType() {
        return User.class;
    }

    public List<User> getFriends(Long userId) {
        return friendStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        return friendStorage.getCommonFriends(userId, friendId);
    }

    public boolean addFriend(Long userId, Long friendId) {
        try {
            friendStorage.addLink(userId, friendId);
        } catch (DataIntegrityViolationException exception) {
            if (exception.getMessage().contains("FRIEND_ID")) {
                throw new DataObjectNotFoundException(friendId);
            } else if (exception.getMessage().contains("USER_ID")) {
                throw new DataObjectNotFoundException(userId);
            }
        }
        return true;
    }

    public boolean deleteFriend(Long userId, Long friendId) {
        if (!contains(userId)) {
            throw new DataObjectNotFoundException(userId);
        } else if (!contains(friendId)) {
            throw new DataObjectNotFoundException(friendId);
        }
        return friendStorage.deleteLink(userId, friendId);
    }

    public List<User> getMostPopularUsers(Integer count) {
        return convertIdListToModelList(friendStorage.getMostPopularObjectId(count));
    }

    public List<User> getMostPopularUsers() {
        return getMostPopularUsers(defaultCountPopularUsers);
    }

    public boolean deleteUser(Long userId) {
        if (!contains(userId)) {
            throw new DataObjectNotFoundException(userId);
        }
        friendStorage.deleteAllFriendsOfUser(userId);
        likeStorage.deleteAllLikesOfUser(userId);
        return super.delete(userId);
    }
}
