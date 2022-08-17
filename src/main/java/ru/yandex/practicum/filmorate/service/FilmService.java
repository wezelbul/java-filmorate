package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.like.InMemoryLikeStorage;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class FilmService extends AbstractDataService<Film, InMemoryFilmStorage> {

    private final InMemoryLikeStorage likeStorage;

    private final Integer defaultCountPopularFilms = 10;

    public FilmService(InMemoryFilmStorage objectStorage, InMemoryLikeStorage likeStorage) {
        super(objectStorage);
        this.likeStorage = likeStorage;
    }

    @Override
    protected Class<Film> getClassType() {
        return Film.class;
    }

    public Collection<Film> getFriends(Long userId) {
        return convertIdSetToModelCollection(likeStorage.getValue(userId));
    }

    public boolean addLike(Long filmId, Long userId) {
        return likeStorage.addLink(filmId, userId);
    }

    public boolean deleteLike(Long filmId, Long userId) {
        return likeStorage.deleteLink(filmId, userId);
    }

    public List<Film> getMostPopularFilms(Integer count) {
        if (count == null) {
            return getMostPopularFilms();
        }
        List<Film> likedFilms = new LinkedList<>(convertIdSetToModelCollection(likeStorage.getMostPopularObjectId(count)));
        if (likedFilms.size() < count) {
            count -= likedFilms.size();
            List<Film> unlikedFilms = new ArrayList<>(getAll());
            unlikedFilms.removeAll(likedFilms);
            unlikedFilms = unlikedFilms.stream().limit(count).collect(Collectors.toList());
            likedFilms.addAll(unlikedFilms);
        }
        return likedFilms;
    }

    public List<Film> getMostPopularFilms() {
        return getMostPopularFilms(defaultCountPopularFilms);
    }

}
