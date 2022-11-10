package ru.yandex.practicum.filmorate.storage.like;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.base.link.LinkStorage;

import java.util.List;

public interface LikeStorage extends LinkStorage {
    List<Film> getMostPopularFilms(Integer count);
    List<Film> getMostPopularFilmsGenre(Integer count, Integer genreId);
    List<Film> getMostPopularFilmsYear(Integer count, Integer year);
    List<Film> getMostPopularFilmsGenreYear(Integer count, Integer genreId, Integer year);
    List<Film> getMostCommonFilms(Long userId, Long friendId);
    boolean deleteAllLikesOfFilm(Long idFilm);
    boolean deleteAllLikesOfUser(Long idUser);
}
