package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.util.UtilReader;

public enum FilmRequests {
    SELECT_ALL("select_all.sql"),
    SELECT_BY_ID("select_by_id.sql"),
    SELECT_FAVORITE_FILMS_USER_ID("like/" + "select_favorite_movies_user_by_id.sql"),
    SELECT_RECOMMENDATIONS_FILMS_ID("select_recommendations_films_id.sql"),
    INSERT("insert.sql"),
    UPDATE("update.sql"),
    DELETE("delete_film.sql"),
    CONTAINS("contains.sql");
    ;

    private static final String SQL_QUERY_DIR = "src/main/resources/sql/query/film/";
    private final String fileName;
    private String sqlQuery;

    FilmRequests(String fileName) {
        this.fileName = fileName;
    }

    public String getSqlQuery() {
        if (this.sqlQuery == null) {
            this.sqlQuery = UtilReader.readString(SQL_QUERY_DIR + this.fileName);
        }
        return this.sqlQuery;
    }
}
