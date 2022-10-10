package ru.yandex.practicum.filmorate.storage.search;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface SearchStorage {
    List<Film> getFoundFilms( String query,List<String> by);
}