package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

public interface MpaRatingStorage {

    List<MpaRating> getMpaRatingList();
    MpaRating getMpaRating(Integer ratingId);
    boolean contains(Integer ratingId);


}
