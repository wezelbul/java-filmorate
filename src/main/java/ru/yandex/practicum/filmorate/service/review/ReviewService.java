package ru.yandex.practicum.filmorate.service.review;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.exception.base.DataObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.film.DbFilmStorage;
import ru.yandex.practicum.filmorate.storage.review.DbReviewStorage;
import ru.yandex.practicum.filmorate.storage.user.DbUserStorage;

/**
 * Сервисы для отзывов
 * CRUD операции с отзывами к фильму, рейтинг отзывов по оценкам пользователей
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final DbFilmStorage dbFilmStorage;
    private final DbUserStorage dbUserStorage;
    private final DbReviewStorage dbReviewStorage;

    // Добавление нового отзыва
    public Review createReview(Review review) {
        checkingForExistenceUser(review.getUserId());
        checkingForExistenceFilm(review.getFilmId());
        log.info("Создание отзыва: {}", review);
        return dbReviewStorage.createReview(review);
    }

    // Редактирование уже имеющегося отзыва
    public Review updateReview(Review review) {
        checkingForExistenceReview(review.getReviewId());
        return dbReviewStorage.updateReview(review);
    }

    // Удаление уже имеющегося отзыва по идентификатору
    public void deleteReviewById(Long reviewId) {
        checkingForExistenceReview(reviewId);
        dbReviewStorage.deleteReviewById(reviewId);
    }

    // Получение отзыва по идентификатору
    public Review getReviewById(Long reviewId) {
        checkingForExistenceReview(reviewId);
        return dbReviewStorage.getReviewById(reviewId);
    }

    // проверка на существование отзыва по идентификатору
    private void checkingForExistenceReview(Long reviewId) {
        if (dbReviewStorage.contains(reviewId)) {
            throw new DataObjectNotFoundException(reviewId);
        }
    }

    // проверка на существование фильма по идентификатору
    private void checkingForExistenceFilm(Long filmId) {
        if (dbFilmStorage.contains(filmId)) {
            throw new DataObjectNotFoundException(filmId);
        }
    }

    // проверка на существование пользователя по идентификатору
    private void checkingForExistenceUser(Long userId) {
        if (dbUserStorage.contains(userId)) {
            throw new DataObjectNotFoundException(userId);
        }
    }
}
