package ru.yandex.practicum.filmorate.exception;

import ru.yandex.practicum.filmorate.model.DataModel;
import ru.yandex.practicum.filmorate.storage.base.data.DataStorage;

public class DataObjectNotFoundException extends RuntimeException {

    public DataObjectNotFoundException(DataModel object) {
        super("Object " + object.getClass().getSimpleName() + " with id=" + object.getId() + " not found");
    }

    public DataObjectNotFoundException(Long id) {
        super("Object with id=" + id + " not found");
    }

    public DataObjectNotFoundException(Long id, DataStorage storage) {
        super("Object with id=" + id + " not found in " + storage.getClass().getSimpleName());
    }

}
