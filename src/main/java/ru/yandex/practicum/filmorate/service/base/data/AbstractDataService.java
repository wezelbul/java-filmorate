package ru.yandex.practicum.filmorate.service.base.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.base.DataObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.DataModel;
import ru.yandex.practicum.filmorate.storage.base.data.DataStorage;

import java.util.ArrayList;
import java.util.List;

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
        if (!contains(id)) {
            throw new DataObjectNotFoundException(id);
        }
        log.debug("Try to get {}-object with id={}", getClassType().getSimpleName(), id);
        Model object = objectStorage.getById(id);
        log.debug("Return {}-object with id={}", object.getClass().getSimpleName(), object.getId());
        return object;
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
        if (contains(object.getId())) {
            return objectStorage.update(object);
        } else {
            throw new DataObjectNotFoundException(object.getClass(), object.getId());
        }
    }

    protected List<Model> convertIdListToModelList(List<Long> idSet) {
        List<Model> result = new ArrayList<>();
        for (Long id : idSet) {
            result.add(objectStorage.getById(id));
        }
        return result;
    }

    @Override
    public boolean delete(Long id) {
        return objectStorage.delete(id);
    }
}