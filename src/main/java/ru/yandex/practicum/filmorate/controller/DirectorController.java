package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.director.DirectorService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/directors")
public class DirectorController {

    private final DirectorService directorService;

    @GetMapping
    public ResponseEntity<List<Director>> read() {
        return ResponseEntity.ok(directorService.getAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Director> read(@Valid @PathVariable Integer id) {
        return ResponseEntity.ok(directorService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Director> create(@Valid @RequestBody Director director){
        return ResponseEntity.status(HttpStatus.CREATED).body(directorService.add(director));
    }

    @PutMapping
    public ResponseEntity<Director> update(@Valid @RequestBody Director director) {
        boolean isContains = directorService.contains(director.getId());
        Director updateDirector = directorService.update(director);
        if (isContains) {
            return ResponseEntity.ok(updateDirector);
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(updateDirector);
        }
    }

    @DeleteMapping(value = "/{id}")
    public boolean delete(@Valid @PathVariable Integer id) {
        return directorService.delete(id);
    }
}
