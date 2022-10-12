package ru.yandex.practicum.filmorate.storage.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.model.Review;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import static ru.yandex.practicum.filmorate.storage.review.ReviewRequests.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class DbReviewStorage implements ReviewStorage {

    private final JdbcTemplate reviews;

    @Autowired
    public DbReviewStorage(JdbcTemplate reviews) {
        this.reviews = reviews;
    }

    // Добавление нового отзыва
    @Override
    public Review createReview(Review review) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        reviews.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(R_INSERT.getSqlQuery(), Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, review.getContent());
            stmt.setBoolean(2, review.getIsPositive());
            stmt.setLong(3, review.getUserId());
            stmt.setLong(4, review.getFilmId());
            stmt.setInt(5, review.getUseful());
            return stmt;
        }, keyHolder);

        return getReviewById(keyHolder.getKey().longValue());
    }

    // Получение определённого количество отзывов
    @Override
    public List<Review> getReview(Integer count) {
        return reviews.query(R_SELECT.getSqlQuery(), DbReviewStorage::makeReview, count);
    }

    // Получение отзыва по идентификатору
    @Override
    public Review getReviewById(Long reviewId) {
        List<Review> review = reviews.query(R_SELECT_BY_REVIEW_ID.getSqlQuery(), DbReviewStorage::makeReview, reviewId);

        return review.stream().findAny().orElse(null);
    }

    @Override
    public Review getReviewByUserIdAndFilmId(Long userId, Long filmId) {
        return reviews.query(R_SELECT_BY_USER_ID_FILM_ID.getSqlQuery(), DbReviewStorage::makeReview, userId, filmId)
                .stream().findAny().orElse(null);
    }

    // Получение определённое количество отзывов по идентификатору фильма
    @Override
    public List<Review> getReviewByFilmId(Long filmId, Integer count) {
        return reviews.query(R_SELECT_BY_FILM_ID.getSqlQuery(), DbReviewStorage::makeReview, filmId, count);
    }

    // Редактирование уже имеющегося отзыва
    @Override
    public Review updateReview(Review review) {
        reviews.update(R_UPDATE.getSqlQuery(),
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId());

        return getReviewById(review.getReviewId());
    }

    // Удаление уже имеющегося отзыва по идентификатору
    @Override
    public void deleteReviewById(Long reviewId) {
        reviews.update(R_DELETE.getSqlQuery(), reviewId);
    }

    // Пользователь ставит лайк отзыву
    @Override
    public void likeReview(Long reviewId, Long userId) {
        reviews.update(L_INSERT.getSqlQuery(), reviewId, userId, true);

        reviews.update(U_UPDATE_INCREASE.getSqlQuery(), reviewId);
    }

    // Пользователь ставит дизлайк отзыву
    @Override
    public void dislikeReview(Long reviewId, Long userId) {
        reviews.update(L_INSERT.getSqlQuery(), reviewId, userId, false);

        reviews.update(U_UPDATE_DECREASE.getSqlQuery(), reviewId);
    }

    // Пользователь удаляет лайк отзыву
    @Override
    public void deleteLikeReview(Long reviewId, Long userId) {
        reviews.update(L_DELETE.getSqlQuery(), reviewId, userId);

        reviews.update(U_UPDATE_DECREASE.getSqlQuery(), reviewId);
    }

    // Пользователь удаляет дизлайк отзыву
    @Override
    public void deleteDislikeReview(Long reviewId, Long userId) {
        reviews.update(L_DELETE.getSqlQuery(), reviewId, userId);

        reviews.update(U_UPDATE_INCREASE.getSqlQuery(), reviewId);
    }

    // Проверка на существование отзыва
    @Override
    public boolean contains(Long reviewId) {
        return reviews.queryForObject(R_CONTAINS.getSqlQuery(), Boolean.TYPE, reviewId);
    }

    // Создание отзыва (объекта Java)
    static Review makeReview(ResultSet resultSet, long rowNum) throws SQLException {
        return Review.builder()
                .reviewId(resultSet.getLong("review_id"))
                .content(resultSet.getString("content"))
                .isPositive(resultSet.getBoolean("is_positive"))
                .userId(resultSet.getLong("user_id"))
                .filmId(resultSet.getLong("film_id"))
                .useful(resultSet.getInt("useful"))
                .build();
    }
}