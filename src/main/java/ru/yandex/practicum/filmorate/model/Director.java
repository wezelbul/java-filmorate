package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Director {
    @NotNull
    Integer id;
    String name;
}