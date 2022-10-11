package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Event;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EventMapper implements RowMapper<Event> {
    @Override
    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Event(
                rs.getLong("event_id"),
                rs.getLong("user_id"),
                rs.getLong("entity_id"),
                rs.getString("event_type"),
                rs.getString("event_operation"),
                rs.getLong("timestamp"));
    }
}
