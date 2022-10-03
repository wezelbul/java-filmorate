package ru.yandex.practicum.filmorate.storage.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Repository
public class DbReviewStorage implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbReviewStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Добавление нового отзыва
    @Override
    public Review createReview(Review review) {
        final String sqlQueryReview = "" +
                "INSERT INTO reviews (content, is_positive, user_id, film_id, useful) " +
                "VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQueryReview, new String[]{"review_id"});
            stmt.setString(1, review.getContent());
            stmt.setBoolean(2, review.getIsPositive());
            stmt.setLong(3, review.getUserId());
            stmt.setLong(4, review.getFilmId());
            stmt.setInt(5, review.getUseful());
            return stmt;
        }, keyHolder);

        return getReviewById(review.getReviewId());
    }

    // Редактирование уже имеющегося отзыва
    @Override
    public Review updateReview(Review review) {
        final String sqlQuery = "" +
                "UPDATE reviews " +
                "SET content = ?, is_positive = ?, user_id = ?, film_id = ?, useful = ? " +
                "WHERE review_id = ?";

        jdbcTemplate.update(sqlQuery,
                review.getContent(),
                review.getIsPositive(),
                review.getUserId(),
                review.getFilmId(),
                review.getUseful());

        return getReviewById(review.getReviewId());
    }

    // Удаление уже имеющегося отзыва по идентификатору
    @Override
    public void deleteReviewById(Long reviewId) {
        final String sqlQuery = "DELETE FROM reviews WHERE review_id = ?";

        jdbcTemplate.update(sqlQuery, reviewId);
    }

    // Получение отзыва по идентификатору
    @Override
    public Review getReviewById(Long reviewId) {
        final String sqlQueryFilm = "" +
                "SELECT review_id, content, is_positive, user_id, film_id, useful " +
                "FROM reviews " +
                "WHERE review_id = ?";

        List<Review> review = jdbcTemplate.query(sqlQueryFilm, DbReviewStorage::makeReview, reviewId);

        return review.get(0);
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
