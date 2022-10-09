package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.base.data.InMemoryDataStorage;

import java.util.List;

@Repository
public class InMemoryFilmStorage extends InMemoryDataStorage<Film> {

}
