package ru.yandex.practicum.filmorate.storage.base.link;

import ru.yandex.practicum.filmorate.exception.base.DataObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.DataModel;
import ru.yandex.practicum.filmorate.storage.base.data.DataStorage;

import javax.validation.constraints.Positive;
import java.util.*;
import java.util.stream.Collectors;

public abstract class InMemoryLinkStorage<Model extends DataModel, LinkedModel extends DataModel> implements LinkStorage {


    protected Map<Long, List<Long>> idStorage = new HashMap<>();

    protected final DataStorage<Model> modelStorage;

    protected final DataStorage<LinkedModel> linkedModelStorage;

    protected InMemoryLinkStorage(DataStorage<Model> modelStorage, DataStorage<LinkedModel> linkedModelStorage) {
        this.modelStorage = modelStorage;
        this.linkedModelStorage = linkedModelStorage;
    }

    @Override
    public List<Long> getValue(@Positive Long dataObjectId) {
        List<Long> result = idStorage.get(dataObjectId);
        if (result == null) {
            result = new ArrayList<>();
        }
        return result;
    }

    @Override
    public boolean addLink(@Positive Long id, @Positive Long linkedId) {
        if (contains(id, linkedId)) {
            List<Long> newSet = getValue(id);
            newSet.add(linkedId);
            idStorage.put(id, newSet);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteLink(@Positive Long id, @Positive Long linkedId) {
        if (contains(id, linkedId)) {
            List<Long> newList = getValue(id);
            newList.remove(linkedId);
            if (newList.isEmpty()) {
                idStorage.remove(id);
            } else {
                idStorage.put(id, newList);
            }
            return true;
        }
        return false;
    }

    @Override
    public List<Long> getMostPopularObjectId(@Positive Integer count) {
        return idStorage.keySet().stream().sorted(new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                return Integer.compare(idStorage.get(o2).size(), idStorage.get(o1).size());
            }
        })
                .distinct().limit(count).collect(Collectors.toList());
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
