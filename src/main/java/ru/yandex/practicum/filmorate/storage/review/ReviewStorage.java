package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;

/**
 * Интерфейс для хранения отзывов
 * CRUD операции с отзывами к фильму, рейтинг отзывов по оценкам пользователей
 */
public interface ReviewStorage {
    // Добавление нового отзыва
    Review createReview(Review review);

    // Редактирование уже имеющегося отзыва
    Review updateReview(Review review);

    // Удаление уже имеющегося отзыва по идентификатору
    void deleteReviewById(Long reviewId);

    // Получение отзыва по идентификатору
    Review getReviewById(Long reviewId);

    // Проверка на существование отзыва
    boolean contains(Long reviewId);
}
