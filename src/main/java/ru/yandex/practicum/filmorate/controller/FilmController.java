package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController extends AbstractDataController<Film, FilmService> {

    public FilmController(FilmService service) {
        super(service);
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {
        return super.create(film);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<Film>> read() {
        return super.read();
    }

    @Override
    @GetMapping(value = "/{id}")
    public ResponseEntity<Film> read(@Valid @PathVariable Long id) {
        return super.read(id);
    }

    @Override
    @PutMapping
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) {
        return super.update(film);
    }

    @PutMapping(value = "/{id}/like/{userId}")
    public Map<String, Boolean> addLike(@Valid @PathVariable Long id, @Valid @PathVariable Long userId) {
        return Map.of("completed", service.addLike(id, userId));
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public Map<String, Boolean> deleteLike(@Valid @PathVariable Long id, @Valid @PathVariable Long userId) {
        return Map.of("completed", service.deleteLike(id, userId));
    }

    @GetMapping(value = "/popular")
    public ResponseEntity<List<Film>> getMostPopularFilmsFilter(@Valid @RequestParam(required = false, name = "count") Integer count,
                                                                @Valid @RequestParam(required = false, name = "genreId") Integer genreId,
                                                                @Valid @RequestParam(required = false, name = "year") Integer year) {
        return ResponseEntity.ok(service.getMostPopularFilms(count, genreId, year));
    }

    // Общие с другом фильмы
    @GetMapping(value = "/common") //GET /films/common?userId={userId}&friendId={friendId}
    public ResponseEntity<List<Film>> getMostCommonFilms(@Valid @RequestParam(required = false, name = "userId") Long userId,
                                                          @Valid @RequestParam(required = false, name = "friendId") Long friendId) {
        return ResponseEntity.ok(service.getMostCommonFilms(userId, friendId));
    }

    @GetMapping(value = "/director/{directorId}")
    public ResponseEntity<List<Film>> getFilmsByDirector(@Valid @PathVariable Integer directorId,@RequestParam String sortBy){
        return ResponseEntity.ok(service.getFilmsByDirector(directorId,sortBy));
    }

    @GetMapping(value = "/search")
    public ResponseEntity<List<Film>> getFoundFilms(@Valid @NotNull @NotBlank @RequestParam String query,
                                                    @Valid @NotNull @NotEmpty @RequestParam List<String> by) {
        return ResponseEntity.ok(service.getFoundFilms(query,by));
    }

    @DeleteMapping(value = "/{id}")
    public void deleteFilm (@Valid @PathVariable Long id) {
        service.deleteFilm(id);

    }

}
