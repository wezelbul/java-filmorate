package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.util.UtilReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DbGenreStorage implements GenreStorage {

    private final JdbcTemplate genres;
    private static final String SQL_QUERY_DIR = "src/main/resources/sql/query/genre/";
    private static final String SELECT_ALL_SQL_PATH = SQL_QUERY_DIR + "select_all.sql";
    private static final String SELECT_FILM_GENRES_SQL_PATH = SQL_QUERY_DIR + "select_film_genres.sql";
    private static final String SELECT_GENRES_ALL_FILMS_SQL_PATH = SQL_QUERY_DIR + "select_genres_all_films.sql";
    private static final String DELETE_FILM_GENRES_SQL_PATH = SQL_QUERY_DIR + "delete_film_genres.sql";
    private static final String SELECT_BY_ID_SQL_PATH = SQL_QUERY_DIR + "select_by_id.sql";
    private static final String INSERT_SQL_PATH = SQL_QUERY_DIR + "insert.sql";

    public DbGenreStorage(JdbcTemplate genres) {
        this.genres = genres;
    }

    @Override
    public List<Genre> getFilmGenres(Long filmId) {
        return genres.query(UtilReader.readString(SELECT_FILM_GENRES_SQL_PATH), new GenreMapper(), filmId);
    }

    @Override
    public void setGenres(Long filmId, Integer genreId) {
        genres.update(UtilReader.readString(INSERT_SQL_PATH), filmId, genreId);
    }

    @Override
    public List<Genre> getGenres() {
        return new ArrayList<>(genres.query(UtilReader.readString(SELECT_ALL_SQL_PATH), new GenreMapper()));
    }

    @Override
    public List<Map<Long, Genre>> getAllFilmsGenres() {
        return genres.query(UtilReader.readString(SELECT_GENRES_ALL_FILMS_SQL_PATH),
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
        return genres.query(UtilReader.readString(SELECT_BY_ID_SQL_PATH), new GenreMapper(), genreId)
                .stream().findAny().orElse(null);
    }

    @Override
    public void clearFilmGenres(Long filmId) {
        genres.update(UtilReader.readString(DELETE_FILM_GENRES_SQL_PATH), filmId);
    }

    @Override
    public boolean contains(Integer genreId) {
        return getGenre(genreId) != null;
    }
}
