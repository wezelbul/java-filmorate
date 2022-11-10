package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.util.UtilReader;

public enum DirectorRequests {
    SELECT_ALL("select_all.sql"),
    SELECT_BY_ID("select_by_id.sql"),
    INSERT("insert.sql"),
    UPDATE("update.sql"),
    DELETE("delete.sql"),
    DELETE_FROM_FILMS("delete_from_films.sql"),
    SELECT_BY_DIRECTOR_ORDER_BY_RATE("select_all_by_director_order_by_rate.sql"),
    SELECT_BY_DIRECTOR_ORDER_BY_YEAR("select_all_by_director_order_by_year.sql"),
    SELECT_DIRECTORS_BY_FILM("select_directors_by_film.sql"),
    INSERT_INTO_FILM_DIRECTORS("insert_into_film_directors.sql"),
    DELETE_DIRECTOR_FROM_ONE_FILM("delete_director_from_one_film.sql"),
    CONTAINS("contains.sql");

    private static final String SQL_QUERY_DIR = "src/main/resources/sql/query/director/";
    private final String fileName;
    private String sqlQuery;

    DirectorRequests(String fileName) {
        this.fileName = fileName;
    }

    public String getSqlQuery() {
        if (this.sqlQuery == null) {
            this.sqlQuery = UtilReader.readString(SQL_QUERY_DIR + this.fileName);
        }
        return this.sqlQuery;
    }
}
