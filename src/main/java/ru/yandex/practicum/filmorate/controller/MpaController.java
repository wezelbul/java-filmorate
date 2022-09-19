package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.mpa.MpaRatingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaController {

    private final MpaRatingService mpaRatingService;

    public MpaController(MpaRatingService mpaRatingService) {
        this.mpaRatingService = mpaRatingService;
    }

    @GetMapping
    public ResponseEntity<List<MpaRating>> read() {
        return ResponseEntity.ok(mpaRatingService.getAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<MpaRating> read(@Valid @PathVariable Integer id) {
        return ResponseEntity.ok(mpaRatingService.getById(id));
    }

}
