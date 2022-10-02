package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.like.DbLikeStorage;
import ru.yandex.practicum.filmorate.util.UtilReader;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbLikeStorageTest {

    private final DbLikeStorage likeStorage;
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
    void getValueTest() {
        long[] idArray = likeStorage.getValue(2L)
                .stream().mapToLong(l -> l).toArray();
        Assertions.assertArrayEquals(new long[]{1L, 2L}, idArray);
    }

    @Test
    void addLinkTest() {
        long[] idArray = likeStorage.getValue(2L)
                .stream().mapToLong(l -> l).toArray();
        Assertions.assertArrayEquals(new long[]{1, 2L}, idArray);

        likeStorage.addLink(2L,3L);

        idArray = likeStorage.getValue(2L)
                .stream().mapToLong(l -> l).toArray();
        Assertions.assertArrayEquals(new long[]{1L, 2L, 3L}, idArray);
    }

    @Test
    void deleteLinkTest() {
        long[] idArray = likeStorage.getValue(3L)
                .stream().mapToLong(l -> l).toArray();
        Assertions.assertArrayEquals(new long[]{1L, 2L, 3L}, idArray);

        likeStorage.deleteLink(3L,1L);

        idArray = likeStorage.getValue(3L)
                .stream().mapToLong(l -> l).toArray();
        Assertions.assertArrayEquals(new long[]{2L, 3L}, idArray);
    }

    @Test
    @Tag("SkipCleanup")
    void getMostPopularObjectIdTest() {
        long[] idArray = likeStorage.getMostPopularObjectId(2)
                .stream().mapToLong(l -> l).toArray();
        Assertions.assertArrayEquals(new long[]{3L, 2L}, idArray);
    }

    @Test
    @Tag("SkipCleanup")
    void getMostPopularFilmsTest() {
        long[] idArray = likeStorage.getMostPopularFilms(2)
                .stream().mapToLong(Film::getId).toArray();
        Assertions.assertArrayEquals(new long[]{3L, 2L}, idArray);
    }

    @Test
    @Tag("SkipCleanup")
    void getMostPopularFilmsYearTest() {
        long[] idArray = likeStorage.getMostPopularFilmsYear(10, 1969)
                .stream().mapToLong(Film::getId).toArray();
        Assertions.assertEquals(idArray.length, 1);
        Assertions.assertArrayEquals(new long[]{1L}, idArray);
    }

    @Test
    @Tag("SkipCleanup")
    void getMostPopularFilmsGenreIdTest() {
        long[] idArray = likeStorage.getMostPopularFilmsGenre(10, 1)
                .stream().mapToLong(Film::getId).toArray();
        Assertions.assertArrayEquals(new long[]{3L, 1L}, idArray);
    }

    @Test
    @Tag("SkipCleanup")
    void getMostPopularFilmsGenreIdYearTest() {
        long[] idArray = likeStorage.getMostPopularFilmsGenreYear(10, 1, 1969)
                .stream().mapToLong(Film::getId).toArray();
        Assertions.assertArrayEquals(new long[]{1L}, idArray);
    }

}
