package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.util.UtilReader;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {

    private final UserService userService;
    private final JdbcTemplate jdbcTemplate;

    @AfterEach
    private void afterEach() {
        jdbcTemplate.update(UtilReader.readString("src/test/resources/drop.sql"));
        jdbcTemplate.update(UtilReader.readString("src/main/resources/schema.sql"));
        jdbcTemplate.update(UtilReader.readString("src/test/resources/data.sql"));
    }

    @Test
    public void getFriendsTest() {
        long[] idArray = userService.getFriends(1L)
                .stream().mapToLong(User::getId).toArray();
        Assertions.assertArrayEquals(new long[]{2L}, idArray);
    }

    @Test
    public void getCommonFriends() {
        long[] idArray = userService.getCommonFriends(2L, 3L)
                .stream().mapToLong(User::getId).toArray();
        Assertions.assertArrayEquals(new long[]{1L}, idArray);
    }

    @Test
    public void addFriendTest() {
        long[] idArray = userService.getFriends(1L)
                .stream().mapToLong(User::getId).toArray();
        Assertions.assertArrayEquals(new long[]{2L}, idArray);

        userService.addFriend(1L,3L);

        idArray = userService.getFriends(1L)
                .stream().mapToLong(User::getId).toArray();
        Assertions.assertArrayEquals(new long[]{2L, 3L}, idArray);

    }

    @Test
    public void deleteFriendTest() {
        long[] idArray = userService.getFriends(3L)
                .stream().mapToLong(User::getId).toArray();
        Assertions.assertArrayEquals(new long[]{1L, 2L}, idArray);

        userService.deleteFriend(3L, 1L);

        idArray = userService.getFriends(3L)
                .stream().mapToLong(User::getId).toArray();
        Assertions.assertArrayEquals(new long[]{2L}, idArray);
    }
}
