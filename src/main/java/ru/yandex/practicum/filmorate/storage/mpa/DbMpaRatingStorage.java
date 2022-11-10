package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.mapper.MpaRatingMapper;

import static ru.yandex.practicum.filmorate.storage.mpa.MpaRatingRequests.*;

import java.util.List;
@Component
public class DbMpaRatingStorage implements MpaRatingStorage{

    private final JdbcTemplate mpaRating;
    private final MpaRatingMapper mpaRatingMapper = new MpaRatingMapper();

    public DbMpaRatingStorage(JdbcTemplate mpaRating) {
        this.mpaRating = mpaRating;
    }


    @Override
    public List<MpaRating> getMpaRatingList() {
        return mpaRating.query(SELECT_ALL.getSqlQuery(), mpaRatingMapper);
    }

    @Override
    public MpaRating getMpaRating(Integer ratingId) {
        return mpaRating.query(SELECT_BY_ID.getSqlQuery(), mpaRatingMapper, ratingId)
                .stream().findAny().orElse(null);
    }

    @Override
    public boolean contains(Integer ratingId) {
        return mpaRating.queryForObject(CONTAINS.getSqlQuery(), Boolean.TYPE, ratingId);
    }

}
