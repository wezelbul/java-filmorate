package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.base.data.InMemoryDataStorage;

@Component
public class InMemoryFilmStorage extends InMemoryDataStorage<Film> {
}
