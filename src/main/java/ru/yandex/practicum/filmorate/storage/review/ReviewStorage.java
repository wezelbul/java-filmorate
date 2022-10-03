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

    // Получение всех отзывов по идентификатору фильма, если фильм не указан то все. Если кол-во не указано то 10.
    List<Review> getAllReviewByFilmId(Long filmId, Integer count);

    // Получение отзыва по идентификатору
    Review getReviewById(Long reviewId);

    // Редактирование уже имеющегося отзыва
    Review updateReview(Review review);

    // Удаление уже имеющегося отзыва по идентификатору
    void deleteReviewById(Long reviewId);

    // Проверка на существование отзыва
    boolean contains(Long reviewId);
}
