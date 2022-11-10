package ru.yandex.practicum.filmorate.exception.event;

public class EventTypeException extends RuntimeException{

    public EventTypeException(String type) {
        super("The type '" + type + "' is not defined");
    }

}
