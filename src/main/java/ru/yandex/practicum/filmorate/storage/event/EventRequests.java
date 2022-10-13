package ru.yandex.practicum.filmorate.storage.event;

import ru.yandex.practicum.filmorate.util.UtilReader;

public enum EventRequests {

    SELECT_BY_USER_ID("select_by_user_id.sql"),
    SELECT_BY_EVENT_ID("select_by_event_id.sql"),
    SELECT_EVENT_TYPES("select_types.sql"),
    SELECT_EVENT_OPERATIONS("select_operations.sql"),
    INSERT("insert.sql"),
    DELETE_BY_USER_ID("delete_by_user_id.sql");

    private final String SQL_QUERY_DIR = "src/main/resources/sql/query/event/";
    private final String fileName;
    private String sqlQuery;

    EventRequests(String fileName) {
        this.fileName = fileName;
    }

    public String getSqlQuery() {
        if (this.sqlQuery == null) {
            this.sqlQuery = UtilReader.readString(SQL_QUERY_DIR + this.fileName);
        }
        return this.sqlQuery;
    }
}
