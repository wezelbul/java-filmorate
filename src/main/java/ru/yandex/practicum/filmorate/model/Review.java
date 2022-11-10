package ru.yandex.practicum.filmorate.model;
import lombok.*;
import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @PositiveOrZero(message = "ИД отзыва не может быть меньше 0")
    private Long reviewId;

    @NotNull(message = "Отзыв не может быть null")
    @NotBlank(message = "Отзыв не может быть пустой")
    private String content;

    @NotNull(message = "Тип отзыва не может быть null")
    private Boolean isPositive;

    @NotNull(message = "ИД пользователя не может быть null")
    private Long userId;

    @NotNull(message = "ИД фильма не может быть null")
    private Long filmId;

    private int useful;
}