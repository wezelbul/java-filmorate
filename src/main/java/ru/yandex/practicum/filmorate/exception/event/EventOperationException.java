package ru.yandex.practicum.filmorate.exception.event;

public class EventOperationException extends RuntimeException {

    public EventOperationException(String operation) {
        super("The operation '" + operation + "' is not defined");
    }
}
