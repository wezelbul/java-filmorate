package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.film.DbFilmStorage;
import ru.yandex.practicum.filmorate.util.UtilReader;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbFilmStorageTest {

    private final DbFilmStorage filmStorage;
    private final JdbcTemplate jdbcTemplate;

    Film testFilm = new Film(null,
            "Гиперболоид инженера Гарина",
            "Параболоид фельдшера Зулина",
            LocalDate.of(1987, 6, 24),
            220,
            new MpaRating(1, "G"),
            null,
            new ArrayList<>(List.of(new Genre(1, "Комедия"))),
            null);

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
    void getAllTest() {
        long[] idArray = filmStorage.getAll()
                .stream().mapToLong(Film::getId).toArray();
        Assertions.assertArrayEquals(new long[]{1L, 2L, 3L}, idArray);
    }

    @Test
    @Tag("SkipCleanup")
    void getById() {
        Film film = new Film(1L,
                "Бриллиантовая рука",
                "Нетленка Гайдая про зарубежную травматологию",
                LocalDate.of(1969,4, 28),
                100,
                new MpaRating(3, "PG-13"),
                1L,
                new ArrayList<>(List.of(new Genre(1, "Комедия"))),
                null);
        Assertions.assertEquals(film, filmStorage.getById(1L));
    }

    @Test
    @Tag("SkipCleanup")
    void containsTest() {
        Assertions.assertTrue(filmStorage.contains(1L));
    }

    @Test
    @Tag("SkipCleanup")
    void notContainsTest() {
        Assertions.assertFalse(filmStorage.contains(101L));
    }

    @Test
    void addTest() {
        testFilm.setId(4L);
        testFilm.setRate(0L);
        testFilm.setGenres(new ArrayList<>());
        Assertions.assertEquals(testFilm, filmStorage.add(testFilm));
    }

    @Test
    void updateTest() {
        testFilm.setId(1L);
        testFilm.setRate(1L);
        Assertions.assertEquals(testFilm, filmStorage.update(testFilm));
    }

}
