package ru.yandex.practicum.filmorate.storage.like;

import ru.yandex.practicum.filmorate.util.UtilReader;

public enum LikeRequests {

    SELECT_BY_ID("select_by_id.sql"),
    SELECT_COMMON_FILMS("select_common_films.sql"),
    SELECT_COUNT_LIMIT("select_count_limit.sql"),
    SELECT_COUNT_LIMIT_GENRE("select_count_limit_genre.sql"),
    SELECT_COUNT_LIMIT_YEAR("select_count_limit_year.sql"),
    SELECT_COUNT_LIMIT_GENRE_YEAR("select_count_limit_genre_year.sql"),
    SELECT_ID_COUNT_LIMIT("select_id_count_limit.sql"),
    INSERT("insert.sql"),
    DELETE("delete.sql"),
    DELETE_ALL_LIKES_OF_FILM("delete_all_likes_of_film.sql"),
    DELETE_ALL_LIKES_OF_USER("delete_all_likes_of_user.sql");

    private static final String SQL_QUERY_DIR = "src/main/resources/sql/query/film/like/";
    private final String fileName;
    private String sqlQuery;

    LikeRequests(String fileName) {
        this.fileName = fileName;
    }

    public String getSqlQuery() {
        if (this.sqlQuery == null) {
            this.sqlQuery = UtilReader.readString(SQL_QUERY_DIR + this.fileName);
        }
        return this.sqlQuery;
    }

}
