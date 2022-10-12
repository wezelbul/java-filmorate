package ru.yandex.practicum.filmorate.storage.event;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface EventStorage {

    List<Event> getUserEvents(Long userId);
    Event getEventByEventId(Long eventId);
    void addEvent(Long userId, Long entityId, String eventType, String operation, Long timestamp);
    void deleteEventsByUserId(Long userId);
    List<String> getEventTypes();
    List<String> getEventOperations();

}
