package ru.yandex.practicum.filmorate.storage.like;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.base.link.LinkStorage;

import java.util.List;

public interface LikeStorage extends LinkStorage {
    List<Film> getMostPopularFilms(Integer count);

    public boolean deleteAllLikesOfFilm(Long idFilm);

    public boolean deleteAllLikesOfUser(Long idUser);
}
