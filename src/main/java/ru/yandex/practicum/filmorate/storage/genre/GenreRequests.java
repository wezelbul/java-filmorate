package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.util.UtilReader;

public enum GenreRequests {

    SELECT_ALL("select_all.sql"),
    SELECT_FILM_GENRES("select_film_genres.sql"),
    SELECT_GENRES_ALL_FILMS("select_genres_all_films.sql"),
    DELETE_FILM_GENRES("delete_film_genres.sql"),
    SELECT_BY_ID("select_by_id.sql"),
    INSERT("insert.sql"),
    CONTAINS("contains.sql");

    private static final String SQL_QUERY_DIR = "src/main/resources/sql/query/genre/";
    private final String fileName;
    private String sqlQuery;

    GenreRequests(String fileName) {
        this.fileName = fileName;
    }

    public String getSqlQuery() {
        if (this.sqlQuery == null) {
            this.sqlQuery = UtilReader.readString(SQL_QUERY_DIR + this.fileName);
        }
        return this.sqlQuery;
    }

}
