package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.base.data.DataStorage;
import ru.yandex.practicum.filmorate.storage.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.storage.mapper.extractor.FilmExtractor;
import ru.yandex.practicum.filmorate.util.UtilReader;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class DbFilmStorage implements DataStorage<Film> {

    private final JdbcTemplate films;
    private static final String SQL_QUERY_DIR = "src/main/resources/sql/query/film/";
    private static final String SELECT_ALL_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "select_all.sql");
    private static final String SELECT_BY_ID_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "select_by_id.sql");
    private static final String SELECT_FAVORITE_FILMS_USER_ID_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR +
                                                                            "like/" + "select_favorite_movies_user_by_id.sql");
    private static final String SELECT_RECOMMENDATIONS_FILMS_ID_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR +
                                                                            "select_recommendations_films_id.sql");
    private static final String INSERT_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "insert.sql");
    private static final String UPDATE_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "update.sql");
    private static final String DELETE_FILM = UtilReader.readString(SQL_QUERY_DIR + "delete_film.sql");

    public DbFilmStorage(JdbcTemplate films) {
        this.films = films;
    }

    @Override
    public List<Film> getAll() {
        return films.query(SELECT_ALL_SQL_QUERY, new FilmMapper());
    }

    @Override
    public Film getById(Long id) {
        return films.query(SELECT_BY_ID_SQL_QUERY, new FilmExtractor(), id).stream().findAny().orElse(null);
    }

    @Override
    public boolean contains(Long id) {
        return getById(id) != null;
    }

    @Override
    public Film add(Film object) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        films.update(connection -> {

            PreparedStatement preparedStatement = connection
                    .prepareStatement(INSERT_SQL_QUERY,
                            Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, object.getName());
            preparedStatement.setString(2, object.getDescription());
            preparedStatement.setDate(3, Date.valueOf(object.getReleaseDate()));
            preparedStatement.setInt(4, object.getDuration());
            preparedStatement.setInt(5, object.getMpa().getId());
            return preparedStatement;

        }, keyHolder);

        return getById(keyHolder.getKey().longValue());
    }

    @Override
    public Film update(Film object) {
        films.update(UPDATE_SQL_QUERY,
                object.getName(),
                object.getDescription(),
                Date.valueOf(object.getReleaseDate()),
                object.getDuration(),
                object.getMpa().getId(),
                object.getId());
        return getById(object.getId());
    }

    @Override
    public boolean delete(Long filmId) {
        films.update(DELETE_FILM,filmId);
        return true;
    }

    // Список фильмов пользователя, схожих по интересам
    public List<Long> getUsersRecommendations(Long id) {
        return films.queryForList(SELECT_RECOMMENDATIONS_FILMS_ID_SQL_QUERY, Long.class, id, id);
    }

    // список фильмов которые лайкнул пользователь
    public List<Long> getFilmsUserById(Long id) {
        return films.queryForList(SELECT_FAVORITE_FILMS_USER_ID_SQL_QUERY, Long.class, id);
    }
}
