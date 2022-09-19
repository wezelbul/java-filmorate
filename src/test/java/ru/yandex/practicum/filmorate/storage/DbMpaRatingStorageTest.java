package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.mpa.DbMpaRatingStorage;
import ru.yandex.practicum.filmorate.util.UtilReader;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbMpaRatingStorageTest {

    private final DbMpaRatingStorage mpaRatingStorage;
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
    @Tag("SkipCleanup")
    void getMpaRatingListTest() {
        int[] idArray = mpaRatingStorage.getMpaRatingList()
                .stream().mapToInt(MpaRating::getId).toArray();
        Assertions.assertArrayEquals(new int[]{1, 2, 3, 4, 5}, idArray);
    }

    @Test
    @Tag("SkipCleanup")
    void getGenreTest() {
        Assertions.assertEquals(new MpaRating(3, "PG-13"), mpaRatingStorage.getMpaRating(3));
    }

    @Test
    @Tag("SkipCleanup")
    void containsTest() {
        Assertions.assertTrue(mpaRatingStorage.contains(1));
    }

    @Test
    @Tag("SkipCleanup")
    void notContainsTest() {
        Assertions.assertFalse(mpaRatingStorage.contains(10));
    }

}
