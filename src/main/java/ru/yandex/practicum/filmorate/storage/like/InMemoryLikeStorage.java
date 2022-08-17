package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.base.link.InMemoryLinkStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

@Component
public class InMemoryLikeStorage extends InMemoryLinkStorage<Film, User> {

    public InMemoryLikeStorage(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        super(filmStorage, userStorage);
    }
}
