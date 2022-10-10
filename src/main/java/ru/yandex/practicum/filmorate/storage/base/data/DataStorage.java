package ru.yandex.practicum.filmorate.storage.base.data;

import ru.yandex.practicum.filmorate.model.DataModel;

import java.util.List;

public interface DataStorage<Model extends DataModel> {

    List<Model> getAll();

    Model getById(Long id);

    boolean contains(Long id);

    Model add(Model object);

    Model update(Model object);

    boolean delete(Long filmId);

}
