package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.DbFilmStorage;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.DbUserStorage;
import ru.yandex.practicum.filmorate.util.UtilReader;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbUserStorageTest {

    private final DbUserStorage userStorage;
    private final FriendStorage friendStorage;
    private final LikeStorage likeStorage;
    private final JdbcTemplate jdbcTemplate;

    User testUser = new User(
            null,
            "filmorate@yandex.ru",
            "yandex",
            "filmorate",
            LocalDate.of(1997, 9, 23));

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
        long[] idArray = userStorage.getAll()
                .stream().mapToLong(User::getId).toArray();
        Assertions.assertArrayEquals(new long[]{1L, 2L, 3L}, idArray);
    }

    @Test
    @Tag("SkipCleanup")
    void getById() {
        User user = new User(1L,
                "traumatology@email.xyz",
                "trauma",
                "Robert",
                LocalDate.of(1995, 10, 2));
        Assertions.assertEquals(user, userStorage.getById(1L));
    }

    @Test
    @Tag("SkipCleanup")
    void containsTest() {
        Assertions.assertTrue(userStorage.contains(1L));
    }

    @Test
    @Tag("SkipCleanup")
    void notContainsTest() {
        Assertions.assertFalse(userStorage.contains(10L));
    }

    @Test
    void addTest() {
        testUser.setId(4L);
        Assertions.assertEquals(testUser, userStorage.add(testUser));
    }

    @Test
    void updateTest() {
        testUser.setId(1L);
        Assertions.assertEquals(testUser, userStorage.update(testUser));
    }

    @Test
    void deleteTest() {
        friendStorage.deleteAllFriendsOfUser(1L);
        likeStorage.deleteAllLikesOfUser(1L);
        userStorage.delete(1L);
        Assertions.assertTrue(userStorage.getAll().size()==2);
    }
}
