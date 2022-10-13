package ru.yandex.practicum.filmorate.storage.friend;

import ru.yandex.practicum.filmorate.util.UtilReader;

public enum FriendRequests {
    SELECT_BY_ID("friend/" + "select_by_id.sql"),
    SELECT_ALL("friend/" + "select_all.sql"),
    SELECT_COMMON("friend/" + "select_common.sql"),
    SELECT_COUNT_LIMIT("friend/" + "select_count_limit.sql"),
    SELECT_CONFIRMING_STATUS("friend/" + "select_confirming_status.sql"),
    INSERT("friend/" + "insert.sql"),
    DELETE("friend/" + "delete.sql"),
    UPDATE_CONFIRMING_STATUS("friend/" + "update_confirming_status.sql"),
    DELETE_ALL_FRIENDS_OF_USER("friend/" + "delete_all_friends_of_user.sql"),
    DELETE_USER_FROM_ALL_FRIENDS("friend/" + "delete_user_from_all_friends.sql"),
    SELECT_USER_BY_ID("select_by_id.sql");

    private static final String SQL_QUERY_DIR = "src/main/resources/sql/query/user/";
    private final String fileName;
    private String sqlQuery;

    FriendRequests(String fileName) {
        this.fileName = fileName;
    }

    public String getSqlQuery() {
        if (this.sqlQuery == null) {
            this.sqlQuery = UtilReader.readString(SQL_QUERY_DIR + this.fileName);
        }
        return this.sqlQuery;
    }
}
