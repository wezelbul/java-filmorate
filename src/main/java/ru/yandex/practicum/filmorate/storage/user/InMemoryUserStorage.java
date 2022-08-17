package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.base.data.InMemoryDataStorage;

@Repository
public class InMemoryUserStorage extends InMemoryDataStorage<User> {
}
