package ru.yandex.practicum.filmorate.exception.user;

import ru.yandex.practicum.filmorate.exception.base.DataObjectAlreadyExistException;

public class UserAlreadyExistException extends DataObjectAlreadyExistException {
    public UserAlreadyExistException(String key, String value) {
        super("User with " + key + "=" + value + " already exist");
    }
}
