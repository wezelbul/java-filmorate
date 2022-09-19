package ru.yandex.practicum.filmorate.service.mpa;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.base.DataObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.mpa.DbMpaRatingStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaRatingStorage;

import java.util.List;

@Service
public class MpaRatingService {

    private final MpaRatingStorage mpaRatingStorage;

    public MpaRatingService(DbMpaRatingStorage genreStorage) {
        this.mpaRatingStorage = genreStorage;
    }

    public List<MpaRating> getAll() {
        return mpaRatingStorage.getMpaRatingList();
    }

    public MpaRating getById(Integer ratingId) {
        if (!contains(ratingId)) {
            throw new DataObjectNotFoundException(ratingId.longValue());
        }
        return mpaRatingStorage.getMpaRating(ratingId);
    }

    public boolean contains(Integer ratingId) {
        return mpaRatingStorage.contains(ratingId);
    }

}
