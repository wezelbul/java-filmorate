package ru.yandex.practicum.filmorate.exception.base;

import ru.yandex.practicum.filmorate.model.DataModel;
import ru.yandex.practicum.filmorate.storage.base.data.DataStorage;

public class DataObjectNotFoundException extends RuntimeException {

    public <Model extends DataModel> DataObjectNotFoundException(Class<Model> clazz, Long id) {
        super("Object " + clazz.getSimpleName() + " with id=" + id + " not found");
    }

    public DataObjectNotFoundException(Long id) {
        super("Object with id=" + id + " not found");
    }

    public <Model extends DataModel> DataObjectNotFoundException(Long id, DataStorage<Model> storage) {
        super("Object with id=" + id + " not found in " + storage.getClass().getName());
    }

}
