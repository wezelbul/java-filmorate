package ru.yandex.practicum.filmorate.service.base.data;

import ru.yandex.practicum.filmorate.model.DataModel;

import java.util.List;

public interface DataService<Model extends DataModel> {

    List<Model> getAll();

    Model getById(Long id);

    boolean contains(Long id);

    Model add(Model object);

    Model update(Model object);

    boolean delete(Long filmId);

}
