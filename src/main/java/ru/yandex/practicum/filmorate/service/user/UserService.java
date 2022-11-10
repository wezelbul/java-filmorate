package ru.yandex.practicum.filmorate.service.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.base.DataObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.base.data.AbstractDataService;
import ru.yandex.practicum.filmorate.storage.event.DbEventStorage;
import ru.yandex.practicum.filmorate.storage.event.EventStorage;
import ru.yandex.practicum.filmorate.storage.like.DbLikeStorage;
import ru.yandex.practicum.filmorate.storage.film.DbFilmStorage;

import ru.yandex.practicum.filmorate.storage.friend.DbFriendStorage;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.DbUserStorage;

import java.util.*;

@Service
public class UserService extends AbstractDataService<User, DbUserStorage> {

    private final FriendStorage friendStorage;
    private final DbFilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final EventStorage eventStorage;
    private final Integer defaultCountPopularUsers = 10;

    public UserService(DbUserStorage userStorage, DbFilmStorage filmStorage, DbFriendStorage friendStorage,
                       DbLikeStorage likeStorage, DbEventStorage eventStorage) {
        super(userStorage);
        this.friendStorage = friendStorage;
        this.likeStorage = likeStorage;
        this.filmStorage = filmStorage;
        this.eventStorage = eventStorage;
    }

    @Override
    protected Class<User> getClassType() {
        return User.class;
    }

    public List<User> getFriends(Long userId) {
        return friendStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        return friendStorage.getCommonFriends(userId, friendId);
    }

    public boolean addFriend(Long userId, Long friendId) {
        try {
            friendStorage.addLink(userId, friendId);
        } catch (DataIntegrityViolationException exception) {
            if (exception.getMessage().contains("FRIEND_ID")) {
                throw new DataObjectNotFoundException(friendId);
            } else if (exception.getMessage().contains("USER_ID")) {
                throw new DataObjectNotFoundException(userId);
            }
        }
        return true;
    }

    public boolean deleteFriend(Long userId, Long friendId) {
        if (!contains(userId)) {
            throw new DataObjectNotFoundException(userId);
        } else if (!contains(friendId)) {
            throw new DataObjectNotFoundException(friendId);
        }
        return friendStorage.deleteLink(userId, friendId);
    }

    public List<User> getMostPopularUsers(Integer count) {
        return convertIdListToModelList(friendStorage.getMostPopularObjectId(count));
    }

    public List<User> getMostPopularUsers() {
        return getMostPopularUsers(defaultCountPopularUsers);
    }

    public boolean deleteUser(Long userId) {
        if (!contains(userId)) {
            throw new DataObjectNotFoundException(userId);
        }
        friendStorage.deleteAllFriendsOfUser(userId);
        likeStorage.deleteAllLikesOfUser(userId);
        eventStorage.deleteEventsByUserId(userId);
        return super.delete(userId);
    }


    public List<Event> getFeed(Long userId) {
        return eventStorage.getUserEvents(userId);
    }
    
    // список фильмов рекомендуемые пользователю
    public List<Film> getUsersRecommendations(Long id) {
        List<Long> recommendUserFilms = filmStorage.getUsersRecommendations(id);
        List<Long> userFilms = filmStorage.getFilmsUserById(id);
        recommendUserFilms.removeAll(userFilms);
        List<Film> recommendFilms = new ArrayList<>();

        for (Long indexFilm:recommendUserFilms) {
            recommendFilms.add(filmStorage.getById(indexFilm));
        }
        return recommendFilms;
    }
}
