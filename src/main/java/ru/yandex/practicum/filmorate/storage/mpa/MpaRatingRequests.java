package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.util.UtilReader;

public enum MpaRatingRequests {

    SELECT_ALL("select_all.sql"),
    SELECT_BY_ID("select_by_id.sql"),
    CONTAINS("contains.sql");

    private static final String SQL_QUERY_DIR = "src/main/resources/sql/query/mpa_rating/";
    private final String fileName;
    private String sqlQuery;

    MpaRatingRequests(String fileName) {
        this.fileName = fileName;
    }

    public String getSqlQuery() {
        if (this.sqlQuery == null) {
            this.sqlQuery = UtilReader.readString(SQL_QUERY_DIR + this.fileName);
        }
        return this.sqlQuery;
    }

}
