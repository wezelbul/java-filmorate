package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.DbGenreStorage;
import ru.yandex.practicum.filmorate.util.UtilReader;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbGenreStorageTest {

    private final DbGenreStorage genreStorage;
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
    void getFilmGenresTest() {
        int[] idArray = genreStorage.getFilmGenres(3L)
                .stream().mapToInt(Genre::getId).toArray();
        Assertions.assertArrayEquals(new int[]{1, 4}, idArray);
    }

    @Test
    void setGenresTest() {
        int[] idArray = genreStorage.getFilmGenres(3L)
                .stream().mapToInt(Genre::getId).toArray();
        Assertions.assertArrayEquals(new int[]{1, 4}, idArray);

        genreStorage.setGenres(3L, 3);

        idArray = genreStorage.getFilmGenres(3L)
                .stream().mapToInt(Genre::getId).toArray();
        Assertions.assertArrayEquals(new int[]{1, 3, 4}, idArray);
    }

    @Test
    @Tag("SkipCleanup")
    void getGenresTest() {
        int[] idArray = genreStorage.getGenres()
                .stream().mapToInt(Genre::getId).toArray();
        Assertions.assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6}, idArray);
    }

    @Test
    @Tag("SkipCleanup")
    void getAllFilmsGenresTest() {
        Assertions.assertEquals(4, genreStorage.getAllFilmsGenres().size());
    }

    @Test
    @Tag("SkipCleanup")
    void getGenreTest() {
        Assertions.assertEquals(new Genre(3, "Мультфильм"), genreStorage.getGenre(3));
    }

    @Test
    void clearFilmGenresTest() {
        Assertions.assertEquals(2, genreStorage.getFilmGenres(3L).size());
        genreStorage.clearFilmGenres(3L);
        Assertions.assertEquals(0, genreStorage.getFilmGenres(3L).size());
    }

    @Test
    @Tag("SkipCleanup")
    void containsTest() {
        Assertions.assertTrue(genreStorage.contains(6));
    }

    @Test
    @Tag("SkipCleanup")
    void notContainsTest() {
        Assertions.assertFalse(genreStorage.contains(9));
    }


}
