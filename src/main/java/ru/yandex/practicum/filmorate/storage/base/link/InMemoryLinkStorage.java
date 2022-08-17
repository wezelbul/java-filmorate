package ru.yandex.practicum.filmorate.storage.base.link;

import ru.yandex.practicum.filmorate.exception.DataObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.DataModel;
import ru.yandex.practicum.filmorate.storage.base.data.DataStorage;

import javax.validation.constraints.Positive;
import java.util.*;
import java.util.stream.Collectors;

public abstract class InMemoryLinkStorage<Model extends DataModel, LinkedModel extends DataModel> implements LinkStorage {


    protected Map<Long, Set<Long>> idStorage = new HashMap<>();

    protected final DataStorage<Model> modelStorage;

    protected final DataStorage<LinkedModel> linkedModelStorage;

    protected InMemoryLinkStorage(DataStorage<Model> modelStorage, DataStorage<LinkedModel> linkedModelStorage) {
        this.modelStorage = modelStorage;
        this.linkedModelStorage = linkedModelStorage;
    }

    @Override
    public Set<Long> getValue(@Positive Long dataObjectId) {
        Set<Long> result = idStorage.get(dataObjectId);
        if (result == null) {
            result = new TreeSet<>();
        }
        return result;
    }

    @Override
    public boolean addLink(@Positive Long id, @Positive Long linkedId) {
        if (contains(id, linkedId)) {
            Set<Long> newSet = getValue(id);
            newSet.add(linkedId);
            idStorage.put(id, newSet);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteLink(@Positive Long id, @Positive Long linkedId) {
        if (contains(id, linkedId)) {
            Set<Long> newSet = getValue(id);
            newSet.remove(linkedId);
            if (newSet.isEmpty()) {
                idStorage.remove(id);
            } else {
                idStorage.put(id, newSet);
            }
            return true;
        }
        return false;
    }

    @Override
    public Set<Long> getMostPopularObjectId(@Positive Integer count) {
        return new LinkedHashSet<>(idStorage.keySet().stream().sorted(new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                return Integer.compare(idStorage.get(o2).size(), idStorage.get(o1).size());
            }
        })
                .limit(count)
                .collect(Collectors.toSet()));
    }

    private boolean contains(@Positive Long id, @Positive Long linkedId) {
        if (modelStorage.contains(id)) {
            if (linkedModelStorage.contains(linkedId)) {
                return true;
            } else {
                throw new DataObjectNotFoundException(linkedId, linkedModelStorage);
            }
        } else {
            throw new DataObjectNotFoundException(id, modelStorage);
        }
    }

}
