package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/users")
public class UserController extends AbstractDataController<User, UserService> {

    public UserController(UserService service) {
        super(service);
    }

    @Override
    @PostMapping(produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        return super.create(user);
    }

    @Override
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> read() {
        return super.read();
    }

    @Override
    @GetMapping(value = "/{id}")
    public ResponseEntity<User> read(@Valid @PathVariable Long id) {
        return super.read(id);
    }

    @Override
    @PutMapping
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        return super.update(user);
    }

    @GetMapping(value = "/{id}/friends")
    public ResponseEntity<Collection<User>> getFriends(@Valid @PathVariable Long id) {
        return ResponseEntity.ok(service.getFriends(id));
    }

    @GetMapping(value = "/{id}/friends/common/{userId}")
    public ResponseEntity<Collection<User>> getFriends(@Valid @PathVariable Long id, @Valid @PathVariable Long userId) {
        return ResponseEntity.ok(service.getCommonFriends(id, userId));
    }

    @PutMapping(value = "/{id}/friends/{userId}")
    public Map<String, Boolean> addFriend(@Valid @PathVariable Long id, @Valid @PathVariable Long userId) {
        return Map.of("completed", service.addFriend(id, userId));
    }

    @DeleteMapping(value = "/{id}/friends/{userId}")
    public Map<String, Boolean> deleteFriend(@Valid @PathVariable Long id, @Valid @PathVariable Long userId) {
        return Map.of("completed", service.deleteFriend(id, userId));
    }

    @GetMapping(value = "{id}/feed")
    public ResponseEntity<Collection<Event>> getFeed(@Valid @PathVariable Long id) {
        return ResponseEntity.ok(service.getFeed(id));
    }

    @GetMapping(value = "/popular?count={count}")
    public ResponseEntity<Collection<User>> getMostPopularUsers(@Valid @RequestParam Integer count) {
        return ResponseEntity.ok(service.getMostPopularUsers(count));
    }

    @GetMapping(value = "/popular")
    public ResponseEntity<Collection<User>> getMostPopularUsers() {
        return ResponseEntity.ok(service.getMostPopularUsers());
    }

    @DeleteMapping(value = "/{id}")
    public void deleteUser (@Valid @PathVariable Long id) {
        service.deleteUser(id);
    }

    // список фильмов рекомендуемые пользователю
    @GetMapping(value = "/{id}/recommendations")
    public List<Film> getUsersRecommendations(@Valid @PathVariable Long id) {
        return service.getUsersRecommendations(id);
    }

}
