package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.InMemoryFriendStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.*;

@Service
public class UserService extends AbstractDataService<User, InMemoryUserStorage> {

    private final InMemoryFriendStorage friendStorage;

    private final Integer defaultCountPopularUsers = 10;

    public UserService(InMemoryUserStorage userStorage, InMemoryFriendStorage friendStorage) {
        super(userStorage);
        this.friendStorage = friendStorage;
    }

    @Override
    protected Class<User> getClassType() {
        return User.class;
    }

    public Collection<User> getFriends(Long userId) {
        return convertIdSetToModelCollection(friendStorage.getValue(userId));
    }

    public Collection<User> getCommonFriends(Long userId, Long friendId) {
        Collection<User> result = getFriends(userId);
        result.retainAll(getFriends(friendId));
        return result;
    }

    public boolean addFriend(Long userId, Long friendId) {
        return friendStorage.addLink(userId, friendId);
    }

    public boolean deleteFriend(Long userId, Long friendId) {
        return friendStorage.deleteLink(userId, friendId);
    }

    public Collection<User> getMostPopularUsers(Integer count) {
        return convertIdSetToModelCollection(friendStorage.getMostPopularObjectId(count));
    }

    public Collection<User> getMostPopularUsers() {
        return getMostPopularUsers(defaultCountPopularUsers);
    }

}
