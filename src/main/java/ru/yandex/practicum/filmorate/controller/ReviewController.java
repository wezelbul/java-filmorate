package ru.yandex.practicum.filmorate.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.review.ReviewService;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер отзывов к фильмам
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    // Добавление нового отзыва
    @PostMapping
    public Review createReview(@Valid @RequestBody Review review) {
        return reviewService.createReview(review);
    }

    // Получение всех отзывов по идентификатору фильма, если фильм не указан то все. Если кол-во не указано, то 10.
    @GetMapping
    public List<Review> getAllReviewByFilmId(
            @RequestParam(required = false) Long filmId,
            @RequestParam(required = false) Integer count) {
        return reviewService.getReview(filmId, count);
    }

    // Получение отзыва по идентификатору
    @GetMapping("/{reviewId}")
    public Review getReviewById(@PathVariable Long reviewId) {
        return reviewService.getReviewById(reviewId);
    }

    // Редактирование уже имеющегося отзыва
    @PutMapping
    public Review updateReview(@Valid @RequestBody Review review) {
        return reviewService.updateReview(review);
    }

    // Удаление уже имеющегося отзыва по идентификатору
    @DeleteMapping("/{reviewId}")
    public void deleteReviewById(@PathVariable("reviewId") Long reviewId) {
        reviewService.deleteReviewById(reviewId);
    }

    // Пользователь ставит лайк отзыву
    @PutMapping("{reviewId}/like/{userId}")
    public void likeReview(@PathVariable Long reviewId, @PathVariable Long userId) {
        reviewService.likeReview(reviewId, userId);
    }

    // Пользователь ставит дизлайк отзыву
    @PutMapping("{reviewId}/dislike/{userId}")
    public void dislikeReview(@PathVariable Long reviewId, @PathVariable Long userId) {
        reviewService.dislikeReview(reviewId, userId);
    }

    // Пользователь удаляет лайк отзыву
    @DeleteMapping("{reviewId}/like/{userId}")
    public void deleteLikeReview(@PathVariable Long reviewId, @PathVariable Long userId) {
        reviewService.deleteLikeReview(reviewId, userId);
    }

    // Пользователь удаляет дизлайк отзыву
    @DeleteMapping("{reviewId}/dislike/{userId}")
    public void deleteDislikeReview(@PathVariable Long reviewId, @PathVariable Long userId) {
        reviewService.deleteDislikeReview(reviewId, userId);
    }
}
