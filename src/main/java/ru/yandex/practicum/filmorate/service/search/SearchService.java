package ru.yandex.practicum.filmorate.service.search;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.search.DbSearchStorage;
import ru.yandex.practicum.filmorate.storage.search.SearchStorage;

import java.util.List;

@Service
public class SearchService {
    private final SearchStorage searchStorage;
    public SearchService(DbSearchStorage searchStorage){this.searchStorage=searchStorage;}

    public List<Film> getFoundFilms(String query,List<String> by){
        return searchStorage.getFoundFilms(query,by);
    }
}
