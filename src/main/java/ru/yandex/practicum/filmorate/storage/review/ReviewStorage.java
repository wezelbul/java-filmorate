package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

/**
 * Интерфейс для хранения отзывов
 * CRUD операции с отзывами к фильму, рейтинг отзывов по оценкам пользователей
 */
public interface ReviewStorage {
    // Добавление нового отзыва
    Review createReview(Review review);

    // Получение определённого количество отзывов
    List<Review> getReview(Integer count);

    // Получение отзыва по идентификатору
    Review getReviewById(Long reviewId);

    // Получение отзыва по идентификаторам пользователя и фильма
    Review getReviewByUserIdAndFilmId(Long userId, Long filmId);

    // Получение определённое количество отзывов по идентификатору фильма
    List<Review> getReviewByFilmId(Long filmId, Integer count);

    // Редактирование уже имеющегося отзыва
    Review updateReview(Review review);

    // Удаление уже имеющегося отзыва по идентификатору
    void deleteReviewById(Long reviewId);

    // Пользователь ставит лайк отзыву
    void likeReview(Long reviewId, Long userId);

    // Пользователь ставит дизлайк отзыву
    void dislikeReview(Long reviewId, Long userId);

    // Пользователь удаляет лайк отзыву
    void deleteLikeReview(Long reviewId, Long userId);

    // Пользователь удаляет дизлайк отзыву
    void deleteDislikeReview(Long reviewId, Long userId);

    // Проверка на существование отзыва
    boolean contains(Long reviewId);
}
