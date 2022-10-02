package ru.yandex.practicum.filmorate.service.review;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.storage.film.DbFilmStorage;
import ru.yandex.practicum.filmorate.storage.review.DbReviewStorage;
import ru.yandex.practicum.filmorate.storage.user.DbUserStorage;

/**
 * Сервисы для отзывов
 * CRUD операции с отзывами к фильму, ретин отзывов по оценкам пользователей
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final DbFilmStorage dbFilmStorage;
    private final DbUserStorage dbUserStorage;
    private final DbReviewStorage dbReviewStorage;

}
