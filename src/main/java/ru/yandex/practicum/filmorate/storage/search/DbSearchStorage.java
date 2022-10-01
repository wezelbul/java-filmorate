package ru.yandex.practicum.filmorate.storage.search;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.search.SearchEmptyRequestParam;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.util.UtilReader;

import java.util.List;

@Repository
public class DbSearchStorage implements SearchStorage  {

    private final JdbcTemplate filmSearch;
    private static final String SQL_QUERY_DIR = "src/main/resources/sql/query/film/search/";
    private static final String SELECT_SEARCH_TITLE_DIRECTOR =
            UtilReader.readString(SQL_QUERY_DIR + "select_search_title_director.sql");
    private static final String SELECT_SEARCH_TITLE =
            UtilReader.readString(SQL_QUERY_DIR + "select_search_title.sql");
    private static final String SELECT_SEARCH_DIRECTOR =
            UtilReader.readString(SQL_QUERY_DIR + "select_search_director.sql");
    public DbSearchStorage(JdbcTemplate filmSearch){
        this.filmSearch=filmSearch;
    }
    @Override
    public List<Film> getFoundFilms(String query, List<String> by) {
        if(query==null||query.isEmpty()){
            throw new SearchEmptyRequestParam();
        }
        if(by==null||by.isEmpty()){
            throw new SearchEmptyRequestParam();
        }
        if(by.size()==2){
            return filmSearch.query(SELECT_SEARCH_TITLE_DIRECTOR, new FilmMapper(),query,"title","director");
        }else if(by.get(0).equals("title")){
            return filmSearch.query(SELECT_SEARCH_TITLE, new FilmMapper(),query);
        }else{
            return filmSearch.query(SELECT_SEARCH_DIRECTOR, new FilmMapper(),query);
        }
    }
}
