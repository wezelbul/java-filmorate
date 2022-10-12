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

import static ru.yandex.practicum.filmorate.storage.director.DirectorRequests.*;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DbDirectorStorage implements DirectorStorage {

    private final JdbcTemplate directors;
    private final GenreStorage genreStorage;
    private final DirectorMapper directorMapper = new DirectorMapper();
    private final FilmMapper filmMapper = new FilmMapper();

    @Override
    public List<Director> getDirectorList() {
        return directors.query(SELECT_ALL.getSqlQuery(), directorMapper);
    }

    @Override
    public Director getDirectorById(Integer directorId) {
        return directors.query(SELECT_BY_ID.getSqlQuery(), directorMapper, directorId)
                .stream().findAny().orElse(null);
    }

    @Override
    public Director add(Director object) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        directors.update(connection -> {

            PreparedStatement preparedStatement = connection
                    .prepareStatement(INSERT.getSqlQuery(),
                            Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, object.getName());
            return preparedStatement;
        }, keyHolder);
        return getDirectorById(keyHolder.getKey().intValue());
    }

    @Override
    public Director update(Director object) {
        directors.update(UPDATE.getSqlQuery(),
                object.getName(),
                object.getId());
        return getDirectorById(object.getId());
    }

    @Override
    public boolean delete(Integer directorId) {
        directors.update(DELETE_FROM_FILMS.getSqlQuery(), directorId);
        directors.update(DELETE.getSqlQuery(), directorId);
        return true;
    }

    @Override
    public List<Film> getFilmsByDirector(Integer directorId, String order){
        if(!contains(directorId)){
            throw new DataObjectNotFoundException(directorId.longValue());
        }
        List<Film> films;
        if (order.equals("likes")){
            films = directors.query(SELECT_BY_DIRECTOR_ORDER_BY_RATE.getSqlQuery(), filmMapper, directorId);
        } else {
            films = directors.query(SELECT_BY_DIRECTOR_ORDER_BY_YEAR.getSqlQuery(), filmMapper, directorId);
        }
        films.forEach(f -> f.setDirectors(getDirectorsByFilm(f)));
        films.forEach(f -> f.setGenres(genreStorage.getFilmGenres(f.getId())));
        return films;
    }

    @Override
    public List<Director> getDirectorsByFilm(Film film) {
        List<Integer> id = directors.queryForList(SELECT_DIRECTORS_BY_FILM.getSqlQuery(), Integer.class, film.getId());
        return id.stream().map(this::getDirectorById).collect(Collectors.toList());
    }

    @Override
    public void createDirectorByFilm(Long filmId, Integer directorId) {
        directors.update(INSERT_INTO_FILM_DIRECTORS.getSqlQuery(), filmId, directorId);
    }

    @Override
    public void deleteDirectorFromOneFilm(Integer id) {
        directors.update(DELETE_DIRECTOR_FROM_ONE_FILM.getSqlQuery(), id);
    }

    @Override
    public boolean contains(Integer directorId) {
        return directors.queryForObject(CONTAINS.getSqlQuery(), Boolean.TYPE, directorId);
    }


}
