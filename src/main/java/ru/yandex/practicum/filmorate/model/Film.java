package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.validation.annotation.AfterCinemaBirthday;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Slf4j
public class Film implements DataModel {

    Long id;

    @NotNull(message = "Name can not be null")
    @NotBlank(message = "Name can not be empty")
    String name;

    @Size(message = "Description is too long", max = 200)
    @NotBlank(message = "Description can not be empty")
    @NotNull(message = "Description can not be null")
    String description;

    @NotNull(message = "Release Date can not be null")
    @AfterCinemaBirthday(message = "The film appeared before cinematography")
    LocalDate releaseDate;

    @NotNull(message = "Duration can not be null")
    @Positive(message = "Duration is negative or zero")
    Integer duration;

    @NotNull(message = "MPA rating can not be null")
    MpaRating mpa;

    Long rate;

    List<Genre> genres;

}
