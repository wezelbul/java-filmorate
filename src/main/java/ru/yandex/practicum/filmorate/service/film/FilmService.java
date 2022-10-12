package ru.yandex.practicum.filmorate.service.film;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.base.DataObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.base.data.AbstractDataService;
import ru.yandex.practicum.filmorate.storage.base.data.DataStorage;
import ru.yandex.practicum.filmorate.storage.director.DbDirectorStorage;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.film.DbFilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.DbGenreStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like.DbLikeStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.DbUserStorage;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class FilmService extends AbstractDataService<Film, DbFilmStorage> {

    private final DataStorage<User> userStorage;
    private final LikeStorage likeStorage;
    private final GenreStorage genreStorage;
    private final DirectorStorage directorStorage;

    private final Integer defaultCountPopularFilms = 10;

    public FilmService(DbFilmStorage filmStorage,
                       DbUserStorage userStorage,
                       DbLikeStorage likeStorage,
                       DbGenreStorage genreStorage,
                       DbDirectorStorage directorStorage) {
        super(filmStorage);
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
        this.genreStorage = genreStorage;
        this.directorStorage = directorStorage;
    }

    @Override
    protected Class<Film> getClassType() {
        return Film.class;
    }

    @Override
    public Film add(Film film) {
        Film result = super.add(film);
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                genreStorage.setGenres(result.getId(), genre.getId());
            }
        }
        if (film.getDirectors() != null) {
            for (Director director : film.getDirectors()) {
                directorStorage.createDirectorByFilm(result.getId(), director.getId());
            }
        }
        result.setGenres(genreStorage.getFilmGenres(result.getId()));
        result.setDirectors(directorStorage.getDirectorsByFilm(result));
        return result;
    }

    public List<User> getLikedUsers(Long userId) {
        List<User> result = new ArrayList<>();
        for (Long id : likeStorage.getValue(userId)) {
            result.add(userStorage.getById(id));
        }
        return result;
    }

    public List<Film> getFilmsByDirector(Integer directorId, String sortBy) {
        return directorStorage.getFilmsByDirector(directorId, sortBy);
    }

    @Override
    public Film update(Film film) {
        genreStorage.clearFilmGenres(film.getId());
        directorStorage.deleteDirectorFromOneFilm(film.getId().intValue());
        Film result = super.update(film);
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                genreStorage.setGenres(film.getId(), genre.getId());
            }
        }
        if (film.getDirectors() != null) {
            for (Director director : film.getDirectors()) {
                directorStorage.createDirectorByFilm(result.getId(), director.getId());
            }
        }
        result.setGenres(genreStorage.getFilmGenres(result.getId()));
        result.setDirectors(directorStorage.getDirectorsByFilm(result));
        return result;
    }

    @Override
    public Film getById(Long id) {
        Film result = super.getById(id);
        result.setGenres(genreStorage.getFilmGenres(result.getId()));
        result.setDirectors(directorStorage.getDirectorsByFilm(result));
        return result;
    }

    @Override
    public List<Film> getAll() {
        Map<Long, Film> filmMap = super.getAll().stream().collect(Collectors.toMap(Film::getId, film -> film));
        for (Map<Long, Genre> map : genreStorage.getAllFilmsGenres()) {
            for (Long filmId : map.keySet()) {
                filmMap.get(filmId).getGenres().add(map.get(filmId));
            }
        }
        for (Film film : filmMap.values()) {
            film.setDirectors(directorStorage.getDirectorsByFilm(film));
        }
        return new ArrayList<>(filmMap.values());
    }

    public boolean addLike(Long filmId, Long userId) {
        try {
            return likeStorage.addLink(filmId, userId);
        } catch (DataIntegrityViolationException exception) {
            if (exception.getMessage().contains("FILM_ID")) {
                throw new DataObjectNotFoundException(filmId);
            } else if (exception.getMessage().contains("USER_ID")) {
                throw new DataObjectNotFoundException(userId);
            }
            return false;
        }
    }

    public boolean deleteLike(Long filmId, Long userId) {
        if (!contains(userId)) {
            throw new DataObjectNotFoundException(userId);
        } else if (!contains(filmId)) {
            throw new DataObjectNotFoundException(userId);
        }
        return likeStorage.deleteLink(filmId, userId);
    }

    public List<Film> getMostPopularFilms(Integer count, Integer genreId, Integer year) {

        if (count == null) {
            count = defaultCountPopularFilms;
        }

        if (genreId == null && year == null) {
            return likeStorage.getMostPopularFilms(count);
        } else {

            if (year == null) {
                return likeStorage.getMostPopularFilmsGenre(count, genreId);
            }

            if (genreId == null) {
                return likeStorage.getMostPopularFilmsYear(count, year);
            }

            return likeStorage.getMostPopularFilmsGenreYear(count, genreId, year);
        }
    }

    public List<Film> getMostCommonFilms(Long userId, Long friendId) {
        return likeStorage.getMostCommonFilms(userId, friendId);
    }

    public boolean deleteFilm(Long filmId) {
        if (!contains(filmId)) {
            throw new DataObjectNotFoundException(filmId);
        }
        genreStorage.clearFilmGenres(filmId);
        likeStorage.deleteAllLikesOfFilm(filmId);
        directorStorage.deleteDirectorFromOneFilm(filmId.intValue());
        return super.delete(filmId);
    }

    public List<Film> getFoundFilms(String query, List<String> by) {
        List<Film> filmList = new ArrayList<>();
        List<Film> allFilms = super.getAll();
        query = query.toLowerCase();

        if (by.size() == 2) {
            filmList.addAll(findFilmByTitle(query, allFilms));
            filmList.addAll(findFilmByDirector(query, allFilms));
        } else if (by.get(0).equals("title")) {
            filmList.addAll(findFilmByTitle(query, allFilms));
        } else {
            filmList.addAll(findFilmByDirector(query, allFilms));
        }
        return filmList.stream()
                .sorted((f1, f2) -> Integer.compare(Math.toIntExact(f2.getRate()), Math.toIntExact(f1.getRate())))
                .collect(Collectors.toList());
    }

    public List<Film> findFilmByTitle(String query, List<Film> allFilms) {
        List<Film> filmList = new ArrayList<>();
        for (Film film : allFilms) {
            if (film.getName().toLowerCase().contains(query)) {
                film.setDirectors(directorStorage.getDirectorsByFilm(film));
                filmList.add(film);
            }
        }
        return filmList;
    }

    public List<Film> findFilmByDirector(String query, List<Film> allFilms) {
        List<Director> DL = directorStorage.getDirectorList();
        List<Film> filmsByDirector = new ArrayList<>();
        for (Director director : DL) {
            if (director.getName().toLowerCase().contains(query)) {
                filmsByDirector.addAll(directorStorage.getFilmsByDirector(director.getId(), "likes"));
            }
        }
        return filmsByDirector;
    }
}
