package ru.yandex.practicum.filmorate.storage.base.data;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.DataModel;
import ru.yandex.practicum.filmorate.exception.base.DataObjectNotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class InMemoryDataStorage<Model extends DataModel> implements DataStorage<Model> {

    protected final Map<Long, Model> objects = new HashMap<>();
    protected Long idCounter = 1L;

    @Override
    public List<Model> getAll() {
        return List.copyOf(objects.values());
    }

    @Override
    public Model getById(@Positive Long id) {
        if (contains(id)) {
            return objects.get(id);
        } else {
            throw new DataObjectNotFoundException(id);
        }
    }

    @Override
    public Model add(@Valid Model object) {
        object.setId(null);
        object.setId(manageAssignId(object));
        objects.put(object.getId(), object);
        return objects.get(object.getId());
    }

    @Override
    public Model update(@Valid Model object) {
        object.setId(manageAssignId(object));
        objects.put(object.getId(), object);
        return objects.get(object.getId());
    }

    @Override
    public boolean contains(@Positive Long id) {
        return objects.containsKey(id);
    }

    private Long manageAssignId(Model object) {
        if (object.getId() == null) {
            if (idCounter != 1L || objects.containsKey(idCounter)) {
                while (objects.containsKey(idCounter)) {
                    ++idCounter;
                }
            }
            return idCounter;
        } else {
            if (object.getId() > 0) {
                return object.getId();
            } else {
                throw new DataObjectNotFoundException(object.getClass(), object.getId());
            }
        }
    }

    public boolean delete(Long filmId) {
        return false;
    }
}
