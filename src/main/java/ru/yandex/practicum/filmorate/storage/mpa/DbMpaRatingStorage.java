package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.mapper.MpaRatingMapper;
import ru.yandex.practicum.filmorate.util.UtilReader;

import java.util.List;
@Component
public class DbMpaRatingStorage implements MpaRatingStorage{

    private final JdbcTemplate mpaRating;
    private static final String SQL_QUERY_DIR = "src/main/resources/sql/query/mpa_rating/";
    private static final String SELECT_ALL_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "select_all.sql");
    private static final String SELECT_BY_ID_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "select_by_id.sql");

    public DbMpaRatingStorage(JdbcTemplate mpaRating) {
        this.mpaRating = mpaRating;
    }


    @Override
    public List<MpaRating> getMpaRatingList() {
        return mpaRating.query(SELECT_ALL_SQL_QUERY, new MpaRatingMapper());
    }

    @Override
    public MpaRating getMpaRating(Integer ratingId) {
        return mpaRating.query(SELECT_BY_ID_SQL_QUERY, new MpaRatingMapper(), ratingId)
                .stream().findAny().orElse(null);
    }

    @Override
    public boolean contains(Integer ratingId) {
        return getMpaRating(ratingId) != null;
    }

}
