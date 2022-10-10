package ru.yandex.practicum.filmorate.storage.search;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.search.SearchEmptyRequestParam;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.director.DbDirectorStorage;
import ru.yandex.practicum.filmorate.storage.film.DbFilmStorage;
import ru.yandex.practicum.filmorate.util.UtilReader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DbSearchStorage implements SearchStorage {

    private final DbFilmStorage filmStorage;
    private final DbDirectorStorage directorStorage;

    public DbSearchStorage(DbFilmStorage filmStorage, DbDirectorStorage directorStorage) {
        this.filmStorage = filmStorage;
        this.directorStorage = directorStorage;
    }

    @Override
    public List<Film> getFoundFilms(String query, List<String> by) {

        List<Film> filmList = new ArrayList<>();
        List<Film> allFilms = filmStorage.getAll();
        List<Director> DL = directorStorage.getDirectorList();
        query = query.toLowerCase();

        if (query == null || query.isEmpty()) {
            throw new SearchEmptyRequestParam();
        }
        if (by == null || by.isEmpty()) {
            throw new SearchEmptyRequestParam();
        }

        if (by.size() == 2) {
            filmList.addAll(findFilmByTitle(query,allFilms));
            filmList.addAll(findFilmByDirector(query,allFilms));
        } else if (by.get(0).equals("title")) {
            filmList.addAll(findFilmByTitle(query,allFilms));
        } else {
            filmList.addAll(findFilmByDirector(query,allFilms));
        }
        return filmList.stream()
                .sorted((f1, f2) -> Integer.compare(Math.toIntExact(f2.getRate()), Math.toIntExact(f1.getRate())))
                .collect(Collectors.toList());
    }

    public List<Film> findFilmByTitle(String query,List<Film> allFilms){
        List<Film> filmList = new ArrayList<>();
        for (Film film : allFilms) {
            if (film.getName().toLowerCase().contains(query)) {
                film.setDirectors(directorStorage.getDirectorsByFilm(film));
                filmList.add(film);
            }
        }
        return filmList;
    }

    public List<Film> findFilmByDirector(String query,List<Film> allFilms){
        List<Director> DL = directorStorage.getDirectorList();
        List<Film> filmsByDirector = new ArrayList<>();
        for (Director director : DL) {
            if (director.getName().toLowerCase().contains(query)) {
                filmsByDirector.addAll(directorStorage.getFilmsByDirector(director.getId(),"likes"));
            }
        }
        return filmsByDirector;
    }
}