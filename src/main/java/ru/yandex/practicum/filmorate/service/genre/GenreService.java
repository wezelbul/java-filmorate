package ru.yandex.practicum.filmorate.service.genre;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.base.DataObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.DbGenreStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Service
public class GenreService {

    private final GenreStorage genreStorage;

    public GenreService(DbGenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public List<Genre> getAll() {
        return genreStorage.getGenres();
    }

    public Genre getById(Integer genreId) {
        if (!contains(genreId)) {
            throw new DataObjectNotFoundException(genreId.longValue());
        }
        return genreStorage.getGenre(genreId);
    }

    public boolean contains(Integer genreId) {
        return genreStorage.contains(genreId);
    }
}
