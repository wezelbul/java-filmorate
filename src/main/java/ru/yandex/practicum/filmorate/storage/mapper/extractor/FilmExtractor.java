package ru.yandex.practicum.filmorate.storage.mapper.extractor;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FilmExtractor implements ResultSetExtractor<List<Film>> {
    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
        boolean isExit = false; // если isExit истина, то значит конец таблицы
        Film film = new Film();
        List<Genre> genres = new ArrayList<>(); // список жанров

        long filmId = 0; // идентификатор фильма предыдущей записи

        List<Film> films = new ArrayList<>();

        if (rs.next()) {
            while (true) {
                if (filmId != rs.getLong("film_id")) {
                    filmId = rs.getLong("film_id");

                    film = new Film();

                    film.setId(rs.getLong("film_id"));
                    film.setName(rs.getString("name"));
                    film.setDescription(rs.getString("description"));
                    film.setReleaseDate(rs.getDate("release_date").toLocalDate());
                    film.setDuration(rs.getInt("duration_in_minutes"));
                    film.setMpa(new MpaRating(rs.getInt("mpa_rating_id"), rs.getString("mpa_rating_name")));
                    film.setRate(rs.getLong("rate"));
                    film.setGenres(List.of());
                }

                // если поле с id жанра не пустое
                if (rs.getString("GENRE_ID") != null) {
                    genres.add(new Genre(rs.getInt("GENRE_ID"), rs.getString("GENRE_NAME")));
                }

                // переход к следующий записи и проверка является ли запись последней в таблице
                if (!rs.next()) {
                    isExit = true;
                }

                // если запись последняя или относится ли следующая строка к текущему фильму
                if (isExit || filmId != rs.getLong("film_id")) {
                    film.setGenres(genres);
                    films.add(film);
                    genres = new ArrayList<>();
                    if (isExit) {
                        break;
                    }
                }
            }
        }
        return films;
    }
}
