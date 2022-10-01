package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.search.SearchService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")

public class SearchController {
    private final SearchService searchService;
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping(value = "/search")
    public ResponseEntity<List<Film>> getFoundFilms(@Valid @RequestParam String query,
                                                    @Valid @RequestParam List<String> by) {
        return ResponseEntity.ok(searchService.getFoundFilms(query,by));
    }
}
