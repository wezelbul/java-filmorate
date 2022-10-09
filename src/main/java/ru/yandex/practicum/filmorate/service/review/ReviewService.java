package ru.yandex.practicum.filmorate.service.review;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.exception.base.DataObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.film.DbFilmStorage;
import ru.yandex.practicum.filmorate.storage.review.DbReviewStorage;
import ru.yandex.practicum.filmorate.storage.user.DbUserStorage;

import java.util.List;

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

    private final Integer defaultCountReview = 10;

    // Добавление нового отзыва
    public Review createReview(Review review) {
        checkingForExistenceUser(review.getUserId());
        checkingForExistenceFilm(review.getFilmId());
        log.info("Создание отзыва: {}", review);
        return dbReviewStorage.createReview(review);
    }

    // Получение всех отзывов по идентификатору фильма, если фильм не указан, то все. Если кол-во не указано, то 10.
    public List<Review> getReview(Long filmId, Integer count) {
        if (count == null || count == 0) {
            count = defaultCountReview;
        }
        if (filmId == null) {
            log.info("Получение списка из {} отзывов:", count);
            return dbReviewStorage.getReview(count);
        } else {
            checkingForExistenceFilm(filmId);
            log.info("Получение списка из {} отзывов для фильма с ИД: {}", count, filmId);
            return dbReviewStorage.getReviewByFilmId(filmId, count);
        }
    }



    // Получение отзыва по идентификатору
    public Review getReviewById(Long reviewId) {
        checkingForExistenceReview(reviewId);
        log.info("Получение отзыва по ИД: {}", reviewId);
        return dbReviewStorage.getReviewById(reviewId);
    }

    // Редактирование уже имеющегося отзыва
    public Review updateReview(Review review) {
        checkingForExistenceReview(review.getReviewId());
        log.info("Редактирование уже имеющегося отзыва: {}", review);
        return dbReviewStorage.updateReview(review);
    }

    // Удаление уже имеющегося отзыва по идентификатору
    public void deleteReviewById(Long reviewId) {
        checkingForExistenceReview(reviewId);
        log.info("Удаление уже имеющегося отзыва c ИД: {}", reviewId);
        dbReviewStorage.deleteReviewById(reviewId);
    }

    // Пользователь ставит лайк отзыву
    public void likeReview(Long reviewId, Long userId) {
        checkingForExistenceReview(reviewId);
        checkingForExistenceUser(userId);
        log.info("Пользователь: {} ставит лайк отзыву: {}", userId, reviewId);
        dbReviewStorage.likeReview(reviewId, userId);
    }

    // Пользователь ставит дизлайк отзыву
    public void dislikeReview(Long reviewId, Long userId) {
        checkingForExistenceReview(reviewId);
        checkingForExistenceUser(userId);
        log.info("Пользователь: {} ставит дизлайк отзыву: {}", userId, reviewId);
        dbReviewStorage.dislikeReview(reviewId, userId);
    }

    // Пользователь удаляет лайк отзыву
    public void deleteLikeReview(Long reviewId, Long userId) {
        checkingForExistenceReview(reviewId);
        checkingForExistenceUser(userId);
        log.info("Пользователь: {} удаляет лайк отзыву: {}", userId, reviewId);
        dbReviewStorage.deleteLikeReview(reviewId, userId);
    }

    // Пользователь удаляет дизлайк отзыву
    public void deleteDislikeReview(Long reviewId, Long userId) {
        checkingForExistenceReview(reviewId);
        checkingForExistenceUser(userId);
        log.info("Пользователь: {} удаляет дизлайк отзыву: {}", userId, reviewId);
        dbReviewStorage.deleteDislikeReview(reviewId, userId);
    }

    // проверка на существование отзыва по идентификатору
    private void checkingForExistenceReview(Long reviewId) {
        if (!dbReviewStorage.contains(reviewId)) {
            throw new DataObjectNotFoundException(reviewId);
        }
    }

    // проверка на существование фильма по идентификатору
    private void checkingForExistenceFilm(Long filmId) {
        if (!dbFilmStorage.contains(filmId)) {
            throw new DataObjectNotFoundException(filmId);
        }
    }

    // проверка на существование пользователя по идентификатору
    private void checkingForExistenceUser(Long userId) {
        if (!dbUserStorage.contains(userId)) {
            throw new DataObjectNotFoundException(userId);
        }
    }
}
