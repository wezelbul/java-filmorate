package ru.yandex.practicum.filmorate.service.review;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.base.DataObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.base.DevelopmentException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.base.data.DataStorage;
import ru.yandex.practicum.filmorate.storage.film.DbFilmStorage;
import ru.yandex.practicum.filmorate.storage.review.DbReviewStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.user.DbUserStorage;

import java.util.List;

/**
 * Сервисы для отзывов
 * CRUD операции с отзывами к фильму, рейтинг отзывов по оценкам пользователей
 */
@Slf4j
@Service
public class ReviewService {
    private final DataStorage<Film> filmStorage;
    private final DataStorage<User> userStorage;
    private final ReviewStorage reviewStorage;

    private final Integer defaultCountReview = 10;

    public ReviewService(DbFilmStorage filmStorage, DbUserStorage userStorage, DbReviewStorage reviewStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.reviewStorage = reviewStorage;
    }

    // Добавление нового отзыва
    public Review createReview(Review review) {
        if (dataIsExist(Film.class, review.getFilmId()) && dataIsExist(User.class, review.getUserId())) {
            log.info("Создание отзыва: {}", review);
            return reviewStorage.createReview(review);
        } else if (!dataIsExist(Film.class, review.getFilmId())) {
            throw new DataObjectNotFoundException(review.getFilmId());
        } else {
            throw new DataObjectNotFoundException(review.getUserId());
        }
    }

    // Получение всех отзывов по идентификатору фильма, если фильм не указан, то все. Если кол-во не указано, то 10.
    public List<Review> getReview(Long filmId, Integer count) {
        if (count == null || count == 0) {
            count = defaultCountReview;
        }
        if (filmId == null) {
            log.info("Получение списка из {} отзывов:", count);
            return reviewStorage.getReview(count);
        } else {
            if (dataIsExist(Film.class, filmId)) {
                log.info("Получение списка из {} отзывов для фильма с ИД: {}", count, filmId);
                return reviewStorage.getReviewByFilmId(filmId, count);
            } else {
                throw new DataObjectNotFoundException(filmId);
            }
        }
    }

    // Получение отзыва по идентификатору
    public Review getReviewById(Long reviewId) {
        if (dataIsExist(Review.class, reviewId)) {
            log.info("Получение отзыва по ИД: {}", reviewId);
            return reviewStorage.getReviewById(reviewId);
        } else {
            throw new DataObjectNotFoundException(reviewId);
        }
    }

    // Редактирование уже имеющегося отзыва
    public Review updateReview(Review review) {
        if (dataIsExist(review.getClass(), review.getReviewId())) {
            log.info("Редактирование уже имеющегося отзыва: {}", review);
            return reviewStorage.updateReview(review);
        } else {
            throw new DataObjectNotFoundException(review.getReviewId());
        }
    }

    // Удаление уже имеющегося отзыва по идентификатору
    public void deleteReviewById(Long reviewId) {
        if (dataIsExist(Review.class, reviewId)) {
            log.info("Удаление уже имеющегося отзыва c ИД: {}", reviewId);
            reviewStorage.deleteReviewById(reviewId);
        } else {
            throw new DataObjectNotFoundException(reviewId);
        }
    }

    // Пользователь ставит лайк отзыву
    public void likeReview(Long reviewId, Long userId) {
        if (dataIsExist(Review.class, reviewId) && dataIsExist(User.class, userId)) {
            log.info("Пользователь: {} ставит лайк отзыву: {}", userId, reviewId);
            reviewStorage.likeReview(reviewId, userId);
        } else if (!dataIsExist(Review.class, reviewId)) {
            throw new DataObjectNotFoundException(reviewId);
        } else {
            throw new DataObjectNotFoundException(userId);
        }
    }

    // Пользователь ставит дизлайк отзыву
    public void dislikeReview(Long reviewId, Long userId) {
        if (dataIsExist(Review.class, reviewId) && dataIsExist(User.class, userId)) {
            log.info("Пользователь: {} ставит дизлайк отзыву: {}", userId, reviewId);
            reviewStorage.dislikeReview(reviewId, userId);
        } else if (!dataIsExist(Review.class, reviewId)) {
            throw new DataObjectNotFoundException(reviewId);
        } else {
            throw new DataObjectNotFoundException(userId);
        }
    }

    // Пользователь удаляет лайк отзыву
    public void deleteLikeReview(Long reviewId, Long userId) {
        if (dataIsExist(Review.class, reviewId) && dataIsExist(User.class, userId)) {
            log.info("Пользователь: {} удаляет лайк отзыву: {}", userId, reviewId);
            reviewStorage.deleteLikeReview(reviewId, userId);
        } else if (!dataIsExist(Review.class, reviewId)) {
            throw new DataObjectNotFoundException(reviewId);
        } else {
            throw new DataObjectNotFoundException(userId);
        }
    }

    // Пользователь удаляет дизлайк отзыву
    public void deleteDislikeReview(Long reviewId, Long userId) {
        if (dataIsExist(Review.class, reviewId) && dataIsExist(User.class, userId)) {
            log.info("Пользователь: {} удаляет дизлайк отзыву: {}", userId, reviewId);
            reviewStorage.deleteDislikeReview(reviewId, userId);
        } else if (!dataIsExist(Review.class, reviewId)) {
            throw new DataObjectNotFoundException(reviewId);
        } else {
            throw new DataObjectNotFoundException(userId);
        }
    }

    private <T> boolean dataIsExist(Class<T> clazz, Long id) {
        if (Review.class.equals(clazz)) {
            return reviewStorage.contains(id);
        } else if (User.class.equals(clazz)) {
            return userStorage.contains(id);
        } else if (Film.class.equals(clazz)) {
            return filmStorage.contains(id);
        } else {
            log.error("Передан непредусмотренный методом класс: {}", clazz.getName());
            throw new DevelopmentException();
        }
    }
}
