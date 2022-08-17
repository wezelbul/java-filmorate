package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.DataModel;

import java.util.List;

public interface DataController<T extends DataModel> {

    ResponseEntity<List<T>> read();

    ResponseEntity<T> read(Long id);

    ResponseEntity<T> create(T object);

    ResponseEntity<T> update(T object);

}
