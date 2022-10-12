package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.util.UtilReader;

public enum ReviewRequests {

    R_INSERT("insert.sql"),
    R_SELECT("select.sql"),
    R_SELECT_BY_REVIEW_ID("select_by_review_id.sql"),
    R_SELECT_BY_USER_ID_FILM_ID("select_by_user_id_film_id.sql"),
    R_SELECT_BY_FILM_ID("select_by_film_id.sql"),
    R_UPDATE("update.sql"),
    R_DELETE("delete.sql"),
    R_CONTAINS("contains.sql"),
    L_INSERT("like/" + "insert.sql"),
    L_DELETE("like/" + "delete.sql"),
    U_UPDATE_INCREASE("useful/" + "update_increase.sql"),
    U_UPDATE_DECREASE("useful/" + "update_decrease.sql");

    private static final String SQL_QUERY_DIR = "src/main/resources/sql/query/review/";
    private final String fileName;

    ReviewRequests(String fileName) {
        this.fileName = fileName;
    }

    public String getSqlQuery() {
        return UtilReader.readString(SQL_QUERY_DIR + this.fileName);
    }
}
