package ru.yandex.practicum.filmorate.storage.event;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.mapper.EventMapper;
import ru.yandex.practicum.filmorate.util.UtilReader;

import java.util.List;

@Repository
public class DbEventStorage implements EventStorage {

    private final JdbcTemplate events;
    private static final String SQL_QUERY_DIR = "src/main/resources/sql/query/event/";
    private static final String SELECT_BY_USER_ID_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "select_by_user_id.sql");
    private static final String SELECT_BY_EVENT_ID_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "select_by_event_id.sql");
    private static final String SELECT_EVENT_TYPES_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "select_types.sql");
    private static final String SELECT_EVENT_OPERATIONS_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "select_operations.sql");
    private static final String INSERT_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "insert.sql");
    private static final String DELETE_BY_USER_ID_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "delete_by_user_id.sql");

    public DbEventStorage(JdbcTemplate events) {
        this.events = events;
    }

    @Override
    public List<Event> getUserEvents(Long userId) {
        return events.query(SELECT_BY_USER_ID_SQL_QUERY, new EventMapper(), userId);
    }

    @Override
    public Event getEventByEventId(Long eventId) {
        return events.query(SELECT_BY_EVENT_ID_SQL_QUERY, new EventMapper(), eventId).stream().findAny().orElse(null);
    }

    @Override
    public void addEvent(Long userId, Long entityId, String eventType, String operation, Long timestamp) {
        events.update(INSERT_SQL_QUERY,
                userId,
                entityId,
                eventType,
                operation,
                timestamp);
    }

    @Override
    public void deleteEventsByUserId(Long userId) {
        events.update(DELETE_BY_USER_ID_SQL_QUERY, userId);
    }

    @Override
    public List<String> getEventTypes() {
        return events.queryForList(SELECT_EVENT_TYPES_SQL_QUERY, String.class);
    }

    @Override
    public List<String> getEventOperations() {
        return events.queryForList(SELECT_EVENT_OPERATIONS_SQL_QUERY, String.class);
    }
}
