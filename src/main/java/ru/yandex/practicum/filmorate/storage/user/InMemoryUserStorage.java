package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.base.data.InMemoryDataStorage;

@Component
public class InMemoryUserStorage extends InMemoryDataStorage<User> {
}
