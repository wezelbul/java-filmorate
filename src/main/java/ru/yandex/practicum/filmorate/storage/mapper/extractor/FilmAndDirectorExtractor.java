package ru.yandex.practicum.filmorate.storage.mapper.extractor;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

    public class FilmAndDirectorExtractor implements ResultSetExtractor<List<Film>> {
        @Override
        public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
            boolean isExit = false; // если isExit истина, то значит конец таблицы
            Film film = new Film();
            Set<Director> director = new HashSet<>(); // список идентификаторов режиссеров
            Set<Genre> genres = new HashSet<>(); // список жанров

            int directorId = 0; // идентификатор режиссера предыдущей записи
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
                        film.setDirectors(List.of());
                        film.setGenres(List.of());
                    }


                    // если поле с id пользователя не пустое и не содержится в списке like
                    if (rs.getString("DIRECTOR_ID") != null && directorId != rs.getInt("DIRECTOR_ID")) {
                        directorId = rs.getInt("DIRECTOR_ID");
                        director.add(new Director(rs.getInt("DIRECTOR_ID"), rs.getString("DIRECTOR_NAME")));
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
                        film.setDirectors(new ArrayList<>(director));
                        film.setGenres(new ArrayList<>(genres));
                        films.add(film);
                        director = new HashSet<>();
                        genres = new HashSet<>();
                        directorId = 0;
                        filmId = 0;
                        if (isExit) {
                            break;
                        }
                    }
                }
            }
            return films;
        }
    }

