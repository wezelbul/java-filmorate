package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface DirectorStorage {

    List<Director> getDirectorList();
    Director getDirectorById(Integer directorId);
    Director add(Director object);
    Director update(Director object);
    boolean delete(Integer directorId);
    List<Film> getFilmsByDirector(Integer directorId, String order);
    void createDirectorByFilm(Long filmId, Integer directorId);
    List<Director> getDirectorsByFilm(Film film);
    boolean contains(Integer directorId);
    void deleteDirectorFromOneFilm(Integer id);

}
