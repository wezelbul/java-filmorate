package ru.yandex.practicum.filmorate.storage.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.util.UtilReader;
import ru.yandex.practicum.filmorate.model.Review;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DbReviewStorage implements ReviewStorage {

    private final JdbcTemplate reviews;

    // SQL запросы для отзывов (R)
    private static final String R_SQL_QUERY_DIR = "src/main/resources/sql/query/review/";
    private static final String R_INSERT_SQL_QUERY = UtilReader.readString(R_SQL_QUERY_DIR + "insert.sql");
    private static final String R_SELECT_SQL_QUERY = UtilReader.readString(R_SQL_QUERY_DIR + "select.sql");
    private static final String R_SELECT_BY_REVIEW_ID_SQL_QUERY = UtilReader.readString(
            R_SQL_QUERY_DIR + "select_by_review_id.sql");
    private static final String R_SELECT_BY_FILM_ID_SQL_QUERY = UtilReader.readString(
            R_SQL_QUERY_DIR + "select_by_film_id.sql");
    private static final String R_UPDATE_SQL_QUERY = UtilReader.readString(R_SQL_QUERY_DIR + "update.sql");
    private static final String R_DELETE_SQL_QUERY = UtilReader.readString(R_SQL_QUERY_DIR + "delete.sql");

    // SQL запросы по лайкам и дизлайкам для отзывов (L)
    private static final String L_SQL_QUERY_DIR = "src/main/resources/sql/query/review/like/";
    private static final String L_INSERT_SQL_QUERY = UtilReader.readString(L_SQL_QUERY_DIR + "insert.sql");
    private static final String L_DELETE_SQL_QUERY = UtilReader.readString(L_SQL_QUERY_DIR + "delete.sql");

    // SQL запросы по изменению рейтинга для отзывов (U)
    private static final String U_SQL_QUERY_DIR = "src/main/resources/sql/query/review/useful/";
    private static final String U_UPDATE_INCREASE_SQL_QUERY = UtilReader.readString(
            U_SQL_QUERY_DIR + "update_increase.sql");
    private static final String U_UPDATE_DECREASE_SQL_QUERY = UtilReader.readString(
            U_SQL_QUERY_DIR + "update_decrease.sql");

    @Autowired
    public DbReviewStorage(JdbcTemplate reviews) {
        this.reviews = reviews;
    }

    // Добавление нового отзыва
    @Override
    public Review createReview(Review review) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        reviews.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(R_INSERT_SQL_QUERY, new String[]{"review_id"});
            stmt.setString(1, review.getContent());
            stmt.setBoolean(2, review.getIsPositive());
            stmt.setLong(3, review.getUserId());
            stmt.setLong(4, review.getFilmId());
            stmt.setInt(5, review.getUseful());
            return stmt;
        }, keyHolder);

        return getReviewById((Long) keyHolder.getKey());
    }

    // Получение определённого количество отзывов
    @Override
    public List<Review> getReview(Integer count) {
        return reviews.query(R_SELECT_SQL_QUERY, DbReviewStorage::makeReview, count);
    }

    // Получение отзыва по идентификатору
    @Override
    public Review getReviewById(Long reviewId) {
        List<Review> review = reviews.query(R_SELECT_BY_REVIEW_ID_SQL_QUERY, DbReviewStorage::makeReview, reviewId);

        return review.stream().findAny().orElse(null);
    }

    // Получение определённое количество отзывов по идентификатору фильма
    @Override
    public List<Review> getReviewByFilmId(Long filmId, Integer count) {
        return reviews.query(R_SELECT_BY_FILM_ID_SQL_QUERY, DbReviewStorage::makeReview, filmId, count);
    }

    // Редактирование уже имеющегося отзыва
    @Override
    public Review updateReview(Review review) {
        reviews.update(R_UPDATE_SQL_QUERY,
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId());

        return getReviewById(review.getReviewId());
    }

    // Удаление уже имеющегося отзыва по идентификатору
    @Override
    public void deleteReviewById(Long reviewId) {
        reviews.update(R_DELETE_SQL_QUERY, reviewId);
    }

    // Пользователь ставит лайк отзыву
    @Override
    public void likeReview(Long reviewId, Long userId) {
        reviews.update(L_INSERT_SQL_QUERY, reviewId, userId, true);

        reviews.update(U_UPDATE_INCREASE_SQL_QUERY, reviewId);
    }

    // Пользователь ставит дизлайк отзыву
    @Override
    public void dislikeReview(Long reviewId, Long userId) {
        reviews.update(L_INSERT_SQL_QUERY, reviewId, userId, false);

        reviews.update(U_UPDATE_DECREASE_SQL_QUERY, reviewId);
    }

    // Пользователь удаляет лайк отзыву
    @Override
    public void deleteLikeReview(Long reviewId, Long userId) {
        reviews.update(L_DELETE_SQL_QUERY, reviewId, userId);

        reviews.update(U_UPDATE_DECREASE_SQL_QUERY, reviewId);
    }

    // Пользователь удаляет дизлайк отзыву
    @Override
    public void deleteDislikeReview(Long reviewId, Long userId) {
        reviews.update(L_DELETE_SQL_QUERY, reviewId, userId);

        reviews.update(U_UPDATE_INCREASE_SQL_QUERY, reviewId);
    }

    // Проверка на существование отзыва
    @Override
    public boolean contains(Long reviewId) {
        return getReviewById(reviewId) != null;
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