package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.DataObject;
import ru.yandex.practicum.filmorate.service.DataObjectService;

import java.util.*;

public abstract class AbstractDataObjectController<T extends DataObject> implements DataObjectController<T> {


    protected final DataObjectService<T> service = getService();

    protected abstract DataObjectService<T> getService();

    @Override
    public ResponseEntity<T> create(T object) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.add(object));
    }

    @Override
    public ResponseEntity<Collection<T>> read() {
        return ResponseEntity.ok(service.getAll());
    }

    @Override
    public ResponseEntity<T> update(T object) {
        boolean isContains = service.contains(object.getId());
        T resultObject = service.update(object);
        if (isContains) {
            return ResponseEntity.ok(resultObject);
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(resultObject);
        }
    }
}
