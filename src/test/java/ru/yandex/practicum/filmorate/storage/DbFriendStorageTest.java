package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.DbFriendStorage;
import ru.yandex.practicum.filmorate.util.UtilReader;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbFriendStorageTest {

    private final DbFriendStorage friendStorage;
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
        long[] idArray = friendStorage.getValue(1L)
                .stream().mapToLong(l -> l).toArray();
        Assertions.assertArrayEquals(new long[]{2L}, idArray);
    }

    @Test
    void addLinkTest() {
        long[] idArray = friendStorage.getValue(1L)
                .stream().mapToLong(l -> l).toArray();
        Assertions.assertArrayEquals(new long[]{2L}, idArray);

        friendStorage.addLink(1L,3L);

        idArray = friendStorage.getValue(1L)
                .stream().mapToLong(l -> l).toArray();
        Assertions.assertArrayEquals(new long[]{2L, 3L}, idArray);
    }

    @Test
    void deleteLinkTest() {
        long[] idArray = friendStorage.getValue(3L)
                .stream().mapToLong(l -> l).toArray();
        Assertions.assertArrayEquals(new long[]{1L, 2L}, idArray);

        friendStorage.deleteLink(3L,1L);

        idArray = friendStorage.getValue(3L)
                .stream().mapToLong(l -> l).toArray();
        Assertions.assertArrayEquals(new long[]{2L}, idArray);
    }

    @Test
    @Tag("SkipCleanup")
    void getMostPopularObjectIdTest() {
        long[] idArray = friendStorage.getMostPopularObjectId(2)
                .stream().mapToLong(l -> l).toArray();
        Assertions.assertArrayEquals(new long[]{2L, 3L}, idArray);
    }

    @Test
    @Tag("SkipCleanup")
    void getConfirmingStatusTrueTest() {
        Assertions.assertTrue(friendStorage.getConfirmingStatus(1L, 2L));
    }

    @Test
    @Tag("SkipCleanup")
    void getConfirmingStatusFalseTest() {
        Assertions.assertFalse(friendStorage.getConfirmingStatus(3L, 1L));
    }

    @Test
    @Tag("SkipCleanup")
    void getFriendsTest() {
        long[] idArray = friendStorage.getFriends(1L)
                .stream().mapToLong(User::getId).toArray();
        Assertions.assertArrayEquals(new long[]{2L}, idArray);
    }

    @Test
    @Tag("SkipCleanup")
    public void getCommonFriends() {
        long[] idArray = friendStorage.getCommonFriends(2L, 3L)
                .stream().mapToLong(User::getId).toArray();
        Assertions.assertArrayEquals(new long[]{1L}, idArray);
    }


}
