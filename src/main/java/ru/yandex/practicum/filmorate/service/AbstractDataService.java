package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.DataModel;
import ru.yandex.practicum.filmorate.storage.base.data.DataStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public abstract class AbstractDataService
        <
        Model extends DataModel,
        Storage extends DataStorage<Model>
        >
        implements DataService<Model> {

    protected final Storage objectStorage;

    @Autowired
    protected AbstractDataService(Storage objectStorage) {
        this.objectStorage = objectStorage;
    }

    protected abstract Class<Model> getClassType();

    @Override
    public List<Model> getAll() {
        List<Model> list = objectStorage.getAll();
        log.debug("Return {} {}-objects", list.size(), getClassType().getSimpleName());
        return list;
    }

    @Override
    public Model getById(Long id) {
        log.debug("Try to get {}-object with id={}", getClassType().getSimpleName(), id);
        Model object = objectStorage.getById(id);
        log.debug("Return {}-object with id={}", object.getClass().getSimpleName(), object.getId());
        return objectStorage.getById(id);
    }

    @Override
    public boolean contains(Long id) {
        return objectStorage.contains(id);
    }

    @Override
    public Model add(Model object) {
        log.debug(object.toString());
        return objectStorage.add(object);
    }

    @Override
    public Model update(Model object) {
        log.debug(object.toString());
        return objectStorage.update(object);
    }

    protected Set<Model> convertIdSetToModelCollection(Set<Long> idSet) {
        Set<Model> result = new HashSet<>();
        for (Long id : idSet) {
            result.add(objectStorage.getById(id));
        }
        return result;
    }

}
