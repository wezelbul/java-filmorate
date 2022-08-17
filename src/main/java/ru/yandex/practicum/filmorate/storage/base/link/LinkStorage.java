package ru.yandex.practicum.filmorate.storage.base.link;

import java.util.Set;

public interface LinkStorage {

    Set<Long> getValue(Long id);

    boolean addLink(Long id, Long linkedId);

    boolean deleteLink(Long id, Long linkedId);

    Set<Long> getMostPopularObjectId(Integer count);

}
