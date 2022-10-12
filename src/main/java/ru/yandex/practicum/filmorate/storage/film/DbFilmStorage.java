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

import static ru.yandex.practicum.filmorate.storage.film.FilmRequests.*;

@Repository
public class DbFilmStorage implements DataStorage<Film> {

    private final JdbcTemplate films;
    private final FilmMapper filmMapper = new FilmMapper();
    private final FilmExtractor filmExtractor = new FilmExtractor();

    public DbFilmStorage(JdbcTemplate films) {
        this.films = films;
    }

    @Override
    public List<Film> getAll() {
        return films.query(SELECT_ALL.getSqlQuery(), filmMapper);
    }

    @Override
    public Film getById(Long id) {
        return films.query(SELECT_BY_ID.getSqlQuery(), filmExtractor, id).stream().findAny().orElse(null);
    }

    @Override
    public boolean contains(Long id) {
        return films.queryForObject(CONTAINS.getSqlQuery(), Boolean.TYPE, id);
    }

    @Override
    public Film add(Film object) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        films.update(connection -> {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(INSERT.getSqlQuery(),
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
        films.update(UPDATE.getSqlQuery(),
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
        films.update(DELETE.getSqlQuery(),filmId);
        return true;
    }

    // Список фильмов пользователя, схожих по интересам
    public List<Long> getUsersRecommendations(Long id) {
        return films.queryForList(SELECT_RECOMMENDATIONS_FILMS_ID.getSqlQuery(), Long.class, id, id);
    }

    // список фильмов которые лайкнул пользователь
    public List<Long> getFilmsUserById(Long id) {
        return films.queryForList(SELECT_FAVORITE_FILMS_USER_ID.getSqlQuery(), Long.class, id);
    }
}
