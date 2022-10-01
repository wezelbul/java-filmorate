package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.film.DbFilmStorage;
import ru.yandex.practicum.filmorate.storage.like.DbLikeStorage;
import ru.yandex.practicum.filmorate.storage.search.DbSearchStorage;
import ru.yandex.practicum.filmorate.util.UtilReader;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class DbSearchStorageTest {

    private final DbSearchStorage searchStorage;
    private final DbFilmStorage filmStorage;
    private final JdbcTemplate jdbcTemplate;

    @AfterEach
    void afterEach(TestInfo testInfo) {
        if(testInfo.getTags().contains("SkipCleanup")) {
            return;
        }
        jdbcTemplate.update(UtilReader.readString("src/test/resources/drop.sql"));
        jdbcTemplate.update(UtilReader.readString("src/main/resources/schema.sql"));
        jdbcTemplate.update(UtilReader.readString("src/test/resources/data.sql"));
    }

    @Test
    void getFoundFilms() {
        List<String> by = new ArrayList<>();
        by.add("title");
        Assertions.assertTrue(searchStorage.getFoundFilms("Бриллиантовая рука",by).size()==1);
        Assertions.assertTrue(searchStorage.getFoundFilms("рука",by).size()==1);
        Assertions.assertTrue(searchStorage.getFoundFilms("а",by).size()==3);
    }
}