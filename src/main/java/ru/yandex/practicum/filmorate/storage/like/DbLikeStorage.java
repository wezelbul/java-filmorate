package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.util.UtilReader;

import java.util.List;

@Repository
public class DbLikeStorage implements LikeStorage {

    private final JdbcTemplate filmLikes;
    private static final String SQL_QUERY_DIR = "src/main/resources/sql/query/film/like/";
    private static final String SELECT_BY_ID_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "select_by_id.sql");
    private static final String SELECT_COUNT_LIMIT_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "select_count_limit.sql");
    private static final String SELECT_ID_COUNT_LIMIT_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "select_id_count_limit.sql");
    private static final String INSERT_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "insert.sql");
    private static final String DELETE_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "delete.sql");
    private static final String DELETE_ALL_LIKES_OF_FILM_SQL_QUERY = UtilReader.readString(
            SQL_QUERY_DIR + "delete_all_likes_of_film.sql");
    private static final String DELETE_ALL_LIKES_OF_USER_SQL_QUERY = UtilReader.readString(
            SQL_QUERY_DIR + "delete_all_likes_of_user.sql");

    public DbLikeStorage(JdbcTemplate filmLikes) {
        this.filmLikes = filmLikes;
    }

    @Override
    public List<Long> getValue(Long filmId) {
        return filmLikes.queryForList(SELECT_BY_ID_SQL_QUERY, Long.class, filmId);
    }

    @Override
    public boolean addLink(Long filmId, Long userId) throws DataIntegrityViolationException {
        filmLikes.update(INSERT_SQL_QUERY, filmId, userId);
        return true;
    }

    @Override
    public boolean deleteLink(Long filmId, Long userId) {
        filmLikes.update(DELETE_SQL_QUERY, filmId, userId);
        return true;
    }

    @Override
    public List<Long> getMostPopularObjectId(Integer count) {
        return filmLikes.queryForList(SELECT_ID_COUNT_LIMIT_SQL_QUERY, Long.class, count);
    }

    @Override
    public List<Film> getMostPopularFilms(Integer count) {
        return filmLikes.query(SELECT_COUNT_LIMIT_SQL_QUERY, new FilmMapper(), count);
    }

    @Override
    public boolean deleteAllLikesOfFilm(Long filmId) {
        filmLikes.update(DELETE_ALL_LIKES_OF_FILM_SQL_QUERY, filmId);
        return true;
    }

    @Override
    public boolean deleteAllLikesOfUser(Long idUser) {
        filmLikes.update(DELETE_ALL_LIKES_OF_USER_SQL_QUERY, idUser);
        return false;
    }
}
