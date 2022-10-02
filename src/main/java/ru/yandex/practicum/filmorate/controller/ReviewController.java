package ru.yandex.practicum.filmorate.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.review.ReviewService;

import javax.validation.Valid;

/**
 * Контроллер отзыовов к фильмам
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

    // Редактирование уже имеющегося отзыва
    @PutMapping
    public Review updateReview(@Valid @RequestBody Review review) {
        return reviewService.updateReview(review);
    }

    // Удаление уже имеющегося отзыва по идентификатору
    @DeleteMapping("/{reviewId}")
    public void deleteReviewById(@PathVariable("reviewId") long reviewId) {
        reviewService.deleteReviewById(reviewId);
    }

    // Получение отзыва по идентификатору
    @GetMapping("/{reviewId}")
    public Review getReviewById(@PathVariable long reviewId) {
        return reviewService.getReviewById(reviewId);
    }
}
