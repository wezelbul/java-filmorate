package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.mapper.extractor.FilmAndDirectorExtractor;
import ru.yandex.practicum.filmorate.storage.mapper.extractor.FilmExtractor;

import static ru.yandex.practicum.filmorate.storage.like.LikeRequests.*;

import java.util.List;

@Repository
public class DbLikeStorage implements LikeStorage {

    private final JdbcTemplate filmLikes;
    private final FilmExtractor filmExtractor = new FilmExtractor();
    private final FilmAndDirectorExtractor filmAndDirectorExtractor = new FilmAndDirectorExtractor();

    public DbLikeStorage(JdbcTemplate filmLikes) {
        this.filmLikes = filmLikes;

    }

    @Override
    public List<Long> getValue(Long filmId) {
        return filmLikes.queryForList(SELECT_BY_ID.getSqlQuery(), Long.class, filmId);
    }

    @Override
    public boolean addLink(Long filmId, Long userId) throws DataIntegrityViolationException {
        filmLikes.update(INSERT.getSqlQuery(), filmId, userId);
        return true;
    }

    @Override
    public boolean deleteLink(Long filmId, Long userId) {
        filmLikes.update(DELETE.getSqlQuery(), filmId, userId);
        return true;
    }

    @Override
    public List<Long> getMostPopularObjectId(Integer count) {
        return filmLikes.queryForList(SELECT_ID_COUNT_LIMIT.getSqlQuery(), Long.class, count);
    }

    @Override
    public List<Film> getMostPopularFilms(Integer count) {
        return filmLikes.query(SELECT_COUNT_LIMIT.getSqlQuery(), filmAndDirectorExtractor, count);
    }

    @Override
    public List<Film> getMostPopularFilmsGenre(Integer count, Integer genreId) {
        return filmLikes.query(SELECT_COUNT_LIMIT_GENRE.getSqlQuery(), filmExtractor, count, genreId);
    }

    @Override
    public List<Film> getMostPopularFilmsYear(Integer count, Integer year) {
        return filmLikes.query(SELECT_COUNT_LIMIT_YEAR.getSqlQuery(), filmExtractor, count, year);
    }

    @Override
    public List<Film> getMostPopularFilmsGenreYear(Integer count, Integer genreId, Integer year) {
        return filmLikes.query(SELECT_COUNT_LIMIT_GENRE_YEAR.getSqlQuery(), filmExtractor, count, genreId, year);
    }

    @Override
    public List<Film> getMostCommonFilms(Long userId, Long friendId) {
        return filmLikes.query(SELECT_COMMON_FILMS.getSqlQuery(), filmExtractor, userId, friendId);
    }

    @Override
    public boolean deleteAllLikesOfFilm(Long filmId) {
        filmLikes.update(DELETE_ALL_LIKES_OF_FILM.getSqlQuery(), filmId);
        return true;
    }

    @Override
    public boolean deleteAllLikesOfUser(Long idUser) {
        filmLikes.update(DELETE_ALL_LIKES_OF_USER.getSqlQuery(), idUser);
        return false;
    }

}
