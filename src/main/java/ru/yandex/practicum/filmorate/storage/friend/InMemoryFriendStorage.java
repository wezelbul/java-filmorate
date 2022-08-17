package ru.yandex.practicum.filmorate.storage.friend;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.base.link.InMemoryLinkStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

@Component
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
