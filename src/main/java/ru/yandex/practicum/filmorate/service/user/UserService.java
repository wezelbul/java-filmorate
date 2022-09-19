package ru.yandex.practicum.filmorate.service.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.base.DataObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.base.data.AbstractDataService;
import ru.yandex.practicum.filmorate.storage.friend.DbFriendStorage;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.DbUserStorage;

import java.util.*;

@Service
public class UserService extends AbstractDataService<User, DbUserStorage> {

    private final FriendStorage friendStorage;
    private final Integer defaultCountPopularUsers = 10;

    public UserService(DbUserStorage userStorage, DbFriendStorage friendStorage) {
        super(userStorage);
        this.friendStorage = friendStorage;
    }

    @Override
    protected Class<User> getClassType() {
        return User.class;
    }

    public List<User> getFriends(Long userId) {
        //return convertIdListToModelList(friendStorage.getValue(userId));
        return friendStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        return friendStorage.getCommonFriends(userId, friendId);
        //List<User> result = getFriends(userId);
        //result.retainAll(getFriends(friendId));
        //return result;
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

        if (friendStorage.getConfirmingStatus(friendId, userId)) {
            friendStorage.setConfirmingStatus(true, userId, friendId);
            friendStorage.setConfirmingStatus(true, friendId, userId);
        }
        return true;
    }

    public boolean deleteFriend(Long userId, Long friendId) {
        if (!contains(userId)) {
            throw new DataObjectNotFoundException(userId);
        } else if (!contains(friendId)) {
            throw new DataObjectNotFoundException(friendId);
        }
        friendStorage.setConfirmingStatus(false, friendId, userId);
        return friendStorage.deleteLink(userId, friendId);
    }

    public List<User> getMostPopularUsers(Integer count) {
        return convertIdListToModelList(friendStorage.getMostPopularObjectId(count));
    }

    public List<User> getMostPopularUsers() {
        return getMostPopularUsers(defaultCountPopularUsers);
    }

}
