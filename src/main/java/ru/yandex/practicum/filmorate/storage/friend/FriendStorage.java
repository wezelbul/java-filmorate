package ru.yandex.practicum.filmorate.storage.friend;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.base.link.LinkStorage;

import java.util.List;

public interface FriendStorage extends LinkStorage {

    Boolean getConfirmingStatus(Long userId, Long friendId);
    void setConfirmingStatus(boolean status, Long userId, Long friendId);
    List<User> getFriends(Long userId);
    List<User> getCommonFriends(Long userId, Long friendId);

}
