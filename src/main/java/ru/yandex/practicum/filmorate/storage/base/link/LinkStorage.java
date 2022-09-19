package ru.yandex.practicum.filmorate.storage.base.link;

import java.util.List;

public interface LinkStorage {

    List<Long> getValue(Long id);

    boolean addLink(Long id, Long linkedId);

    boolean deleteLink(Long id, Long linkedId);

    List<Long> getMostPopularObjectId(Integer count);

}
