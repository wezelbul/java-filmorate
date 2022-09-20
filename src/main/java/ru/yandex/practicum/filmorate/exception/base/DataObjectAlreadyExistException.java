package ru.yandex.practicum.filmorate.exception.base;


public class DataObjectAlreadyExistException extends RuntimeException {

    public DataObjectAlreadyExistException(String message) {
        super(message);
    }

}
