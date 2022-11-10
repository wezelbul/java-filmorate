package ru.yandex.practicum.filmorate.storage.event;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.mapper.EventMapper;

import static ru.yandex.practicum.filmorate.storage.event.EventRequests.*;

import java.util.List;

@Repository
public class DbEventStorage implements EventStorage {

    private final JdbcTemplate events;
    private final EventMapper eventMapper = new EventMapper();

    public DbEventStorage(JdbcTemplate events) {
        this.events = events;
    }

    @Override
    public List<Event> getUserEvents(Long userId) {
        return events.query(SELECT_BY_USER_ID.getSqlQuery(), eventMapper, userId);
    }

    @Override
    public Event getEventByEventId(Long eventId) {
        return events.query(SELECT_BY_EVENT_ID.getSqlQuery(), eventMapper, eventId).stream().findAny().orElse(null);
    }

    @Override
    public void addEvent(Long userId, Long entityId, String eventType, String operation, Long timestamp) {
        events.update(INSERT.getSqlQuery(),
                userId,
                entityId,
                eventType,
                operation,
                timestamp);
    }

    @Override
    public void deleteEventsByUserId(Long userId) {
        events.update(DELETE_BY_USER_ID.getSqlQuery(), userId);
    }

    @Override
    public List<String> getEventTypes() {
        return events.queryForList(SELECT_EVENT_TYPES.getSqlQuery(), String.class);
    }

    @Override
    public List<String> getEventOperations() {
        return events.queryForList(SELECT_EVENT_OPERATIONS.getSqlQuery(), String.class);
    }
}
