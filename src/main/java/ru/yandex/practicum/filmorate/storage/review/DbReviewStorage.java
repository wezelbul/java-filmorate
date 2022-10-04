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

        return getReviewById((Long) keyHolder.getKey());
    }

    // Получение всех отзывов по идентификатору фильма, если фильм не указан то все. Если кол-во не указано, то 10.
    @Override
    public List<Review> getAllReviewByFilmId(Long filmId, Integer count) {
        // Если есть идентификатор фильма указано
        if (filmId != null) {
            final String sqlQuery = "" +
                    "SELECT review_id, content, is_positive, user_id, film_id, useful " +
                    "FROM reviews " +
                    "WHERE film_id = ?" +
                    "ORDER BY useful DESC LIMIT ?";

            return jdbcTemplate.query(sqlQuery, DbReviewStorage::makeReview, filmId, count);
            // Если идентификатор фильма НЕ указан
        } else {
            final String sqlQuery = "" +
                    "SELECT review_id, content, is_positive, user_id, film_id, useful " +
                    "FROM reviews " +
                    "ORDER BY useful DESC LIMIT ?";

            return jdbcTemplate.query(sqlQuery, DbReviewStorage::makeReview, count);
        }
    }

    // Получение отзыва по идентификатору
    @Override
    public Review getReviewById(Long reviewId) {
        final String sqlQueryFilm = "" +
                "SELECT review_id, content, is_positive, user_id, film_id, useful " +
                "FROM reviews " +
                "WHERE review_id = ?";

        List<Review> review = jdbcTemplate.query(sqlQueryFilm, DbReviewStorage::makeReview, reviewId);

        return review.stream().findAny().orElse(null);
    }

    // Редактирование уже имеющегося отзыва
    @Override
    public Review updateReview(Review review) {
        final String sqlQuery = "" +
                "UPDATE reviews " +
                "SET content = ?, is_positive = ?" +
                "WHERE review_id = ?";

        jdbcTemplate.update(sqlQuery,
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId());

        return getReviewById(review.getReviewId());
    }

    // Удаление уже имеющегося отзыва по идентификатору
    @Override
    public void deleteReviewById(Long reviewId) {
        final String sqlQuery = "DELETE FROM reviews WHERE review_id = ?";

        jdbcTemplate.update(sqlQuery, reviewId);
    }

    // Пользователь ставит лайк отзыву
    @Override
    public void likeReview(Long reviewId, Long userId) {
        final String sqlQueryInsertLike = "INSERT INTO reviews_likes (review_id, user_id, is_like) values (?,?,?)";

        jdbcTemplate.update(sqlQueryInsertLike, reviewId, userId, true);

        final String sqlQueryIncreaseUseful = "UPDATE reviews set useful = useful + 1 where review_id = ?";

        jdbcTemplate.update(sqlQueryIncreaseUseful, reviewId);
    }

    // Пользователь ставит дизлайк отзыву
    @Override
    public void dislikeReview(Long reviewId, Long userId) {
        final String sqlQueryInsertDislike = "INSERT INTO reviews_likes (review_id, user_id, is_like) values (?,?,?)";

        jdbcTemplate.update(sqlQueryInsertDislike, reviewId, userId, false);

        final String sqlQueryDecreaseUseful = "UPDATE reviews set useful = useful - 1 where review_id = ?";

        jdbcTemplate.update(sqlQueryDecreaseUseful, reviewId);
    }

    // Пользователь удаляет лайк отзыву
    @Override
    public void deleteLikeReview(Long reviewId, Long userId) {
        final String sqlQueryDeleteLike = "DELETE FROM reviews_likes WHERE review_id = ? and user_id = ?";

        jdbcTemplate.update(sqlQueryDeleteLike, reviewId, userId);

        final String sqlQueryDecreaseUseful = "UPDATE reviews set useful = useful - 1 where review_id = ?";

        jdbcTemplate.update(sqlQueryDecreaseUseful, reviewId);
    }

    // Пользователь удаляет дизлайк отзыву
    @Override
    public void deleteDislikeReview(Long reviewId, Long userId) {
        final String sqlQueryDeleteDislike = "DELETE FROM reviews_likes WHERE review_id = ? and user_id = ?";

        jdbcTemplate.update(sqlQueryDeleteDislike, reviewId, userId);

        final String sqlQueryIncreaseUseful = "UPDATE reviews set useful = useful + 1 where review_id = ?";

        jdbcTemplate.update(sqlQueryIncreaseUseful, reviewId);
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
