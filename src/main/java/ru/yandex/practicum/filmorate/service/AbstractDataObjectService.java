package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.DataObject;
import ru.yandex.practicum.filmorate.storage.DataObjectStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryDataObjectStorage;

import java.util.Collection;

@Service
@Slf4j
public abstract class AbstractDataObjectService<T extends DataObject> implements DataObjectService<T> {

    protected final DataObjectStorage<T> objectStorage = new InMemoryDataObjectStorage<>();

    protected abstract Class<T> getClassType();

    @Override
    public Collection<T> getAll() {
        Collection<T> collection = objectStorage.getAll();
        log.debug("Return {} {}-objects", collection.size(), getClassType().getSimpleName());
        return collection;
    }

    @Override
    public boolean contains(Long id) {
        return objectStorage.contains(id);
    }

    @Override
    public T add(T object) {
        log.debug(object.toString());
        return objectStorage.add(object);
    }

    @Override
    public T update(T object) {
        log.debug(object.toString());
        return objectStorage.update(object);
    }
}
