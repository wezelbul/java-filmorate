package ru.yandex.practicum.filmorate.storage.director;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.base.DataObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.storage.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.util.UtilReader;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DbDirectorStorage implements DirectorStorage {

    private final JdbcTemplate directors;
    private final GenreStorage genreStorage;

    private static final String SQL_QUERY_DIR = "src/main/resources/sql/query/director/";
    private static final String SELECT_ALL_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "select_all.sql");
    private static final String SELECT_BY_ID_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "select_by_id.sql");
    private static final String INSERT_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "insert.sql");
    private static final String UPDATE_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "update.sql");
    private static final String DELETE_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "delete.sql");
    private static final String DELETE_FROM_FILMS_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "delete_from_films.sql");
    private static final String SELECT_BY_DIRECTOR_ORDER_BY_RATE_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "select_all_by_director_order_by_rate.sql");
    private static final String SELECT_BY_DIRECTOR_ORDER_BY_YEAR_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "select_all_by_director_order_by_year.sql");
    private static final String SELECT_DIRECTORS_BY_FILM_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "select_directors_by_film.sql");
    private static final String INSERT_INTO_FILM_DIRECTORS_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "insert_into_film_directors.sql");
    private static final String DELETE_DIRECTOR_FROM_ONE_FILM_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "delete_director_from_one_film.sql");

    @Override
    public List<Director> getDirectorList() {
        return directors.query(SELECT_ALL_SQL_QUERY, new DirectorMapper());
    }

    @Override
    public Director getDirector(Integer directorId) {
        return directors.query(SELECT_BY_ID_SQL_QUERY, new DirectorMapper(), directorId)
                .stream().findAny().orElse(null);
    }

    @Override
    public Director add(Director object) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        directors.update(connection -> {

            PreparedStatement preparedStatement = connection
                    .prepareStatement(INSERT_SQL_QUERY,
                            Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, object.getName());
            return preparedStatement;
        }, keyHolder);
        return getDirector(keyHolder.getKey().intValue());
    }

    @Override
    public Director update(Director object) {
        directors.update(UPDATE_SQL_QUERY,
                object.getName(),
                object.getId());
        return getDirector(object.getId());
    }

    @Override
    public boolean delete(Integer directorId) {
        directors.update(DELETE_FROM_FILMS_SQL_QUERY, directorId);
        directors.update(DELETE_SQL_QUERY, directorId);
        return true;
    }

    @Override
    public List<Film> getFilmsByDirector(Integer directorId, String order){
        if(!contains(directorId)){
            throw new DataObjectNotFoundException(directorId.longValue());
        }
        List<Film> films;
        if (order.equals("likes")){
            films = directors.query(SELECT_BY_DIRECTOR_ORDER_BY_RATE_SQL_QUERY, new FilmMapper(), directorId);
        } else {
            films = directors.query(SELECT_BY_DIRECTOR_ORDER_BY_YEAR_SQL_QUERY, new FilmMapper(), directorId);
        }
        films.forEach(f -> f.setDirectors(getDirectorsByFilm(f)));
        films.forEach(f -> f.setGenres(genreStorage.getFilmGenres(f.getId())));
        return films;
    }

    @Override
    public List<Director> getDirectorsByFilm(Film film) {
        List<Integer> id = directors.queryForList(SELECT_DIRECTORS_BY_FILM_SQL_QUERY, Integer.class, film.getId());
        return id.stream().map(this::getDirector).collect(Collectors.toList());
    }

    @Override
    public void createDirectorByFilm(Long filmId, Integer directorId) {
        directors.update(INSERT_INTO_FILM_DIRECTORS_SQL_QUERY, filmId, directorId);
    }

    @Override
    public void deleteDirectorFromOneFilm(Integer id) {
        directors.update(DELETE_DIRECTOR_FROM_ONE_FILM_SQL_QUERY, id);
    }

    @Override
    public boolean contains(Integer directorId) {
        return getDirector(directorId) != null;
    }


}
