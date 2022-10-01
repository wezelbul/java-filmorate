package ru.yandex.practicum.filmorate.exception.search;

public class SearchEmptyRequestParam extends RuntimeException {
    public SearchEmptyRequestParam() {
        super("Request param is empty");
    }
}
