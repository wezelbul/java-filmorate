package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;


import java.util.List;

public interface DirectorStorage {

    List<Director> getDirectorList();
    Director getDirector(Integer directorId);

    Director add(Director object);

    Director update(Director object);

    boolean delete(Integer directorId);

    List<Film> getFilmsByDirector(Integer directorId, String order);

    List<Director> getDirectorsByFilm(Film film);

    void updateDirectorFilm(Integer id);
    boolean contains(Integer directorId);
}
