package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;


import java.util.List;
import java.util.Set;

public interface DirectorStorage {

    List<Director> getDirectorList();
    Director getDirector(Integer directorId);

    Director add(Director object);

    Director update(Director object);

    boolean delete(Integer directorId);

    List<Film> getFilmsByDirector(Integer directorId, String order);

    Set<Director> getDirectorsByFilm(Film film);

    void updateDirectorFilm(Integer id);
    boolean contains(Integer directorId);
}
