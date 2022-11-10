package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;

import static ru.yandex.practicum.filmorate.storage.genre.GenreRequests.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DbGenreStorage implements GenreStorage {

    private final JdbcTemplate genres;
    private final GenreMapper genreMapper = new GenreMapper();

    public DbGenreStorage(JdbcTemplate genres) {
        this.genres = genres;
    }

    @Override
    public List<Genre> getFilmGenres(Long filmId) {
        return genres.query(SELECT_FILM_GENRES.getSqlQuery(), genreMapper, filmId);
    }

    @Override
    public void setGenres(Long filmId, Integer genreId) {
        genres.update(INSERT.getSqlQuery(), filmId, genreId);
    }

    @Override
    public List<Genre> getGenres() {
        return new ArrayList<>(genres.query(SELECT_ALL.getSqlQuery(), genreMapper));
    }

    @Override
    public List<Map<Long, Genre>> getAllFilmsGenres() {
        return genres.query(SELECT_GENRES_ALL_FILMS.getSqlQuery(),
                (rs, rowNum) -> {
                    Map<Long, Genre> result = new HashMap<>();
                    result.put(rs.getLong("film_id"),
                            new Genre(rs.getInt("genre_id"),
                                    rs.getString("genre_name")));
                    return result;
                });
    }

    @Override
    public Genre getGenre(Integer genreId) {
        return genres.query(SELECT_BY_ID.getSqlQuery(), genreMapper, genreId)
                .stream().findAny().orElse(null);
    }

    @Override
    public void clearFilmGenres(Long filmId) {
        genres.update(DELETE_FILM_GENRES.getSqlQuery(), filmId);
    }

    @Override
    public boolean contains(Integer genreId) {
        return genres.queryForObject(CONTAINS.getSqlQuery(), Boolean.TYPE, genreId);
    }


}
