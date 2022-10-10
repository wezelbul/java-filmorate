package ru.yandex.practicum.filmorate.exception.search;

public class SearchEmptyRequestParamException extends RuntimeException {
    public SearchEmptyRequestParamException() {
        super("Request param is empty");
    }
}