package ru.yandex.practicum.filmorate.storage.friend;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.base.link.InMemoryLinkStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

@Repository
public class InMemoryFriendStorage extends InMemoryLinkStorage {

    public InMemoryFriendStorage(InMemoryUserStorage storage) {
        super(storage, storage);
    }

    @Override
    public boolean addLink(Long userId, Long friendId) {
        return super.addLink(userId, friendId)
                && super.addLink(friendId, userId);
    }

    @Override
    public boolean deleteLink(Long userId, Long friendId) {
        return super.deleteLink(userId, friendId)
                && super.deleteLink(friendId, userId);
    }

}
