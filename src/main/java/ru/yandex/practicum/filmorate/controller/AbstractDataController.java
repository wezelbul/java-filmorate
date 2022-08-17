package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.DataModel;
import ru.yandex.practicum.filmorate.service.DataService;

import java.util.*;

public abstract class AbstractDataController<Model extends DataModel, Service extends DataService<Model>> implements DataController<Model> {

    protected final Service service;

    @Autowired
    protected AbstractDataController(Service service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<Model> create(Model object) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.add(object));
    }

    @Override
    public ResponseEntity<List<Model>> read() {
        return ResponseEntity.ok(service.getAll());
    }

    @Override
    public ResponseEntity<Model> read(Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Override
    public ResponseEntity<Model> update(Model object) {
        boolean isContains = service.contains(object.getId());
        Model resultObject = service.update(object);
        if (isContains) {
            return ResponseEntity.ok(resultObject);
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(resultObject);
        }
    }
}
