package ru.yandex.practicum.filmorate.model;
import lombok.*;
import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @NotNull
    private long reviewId;
    @NotNull(message = "Отзыв не может быть пустой")
    @NotBlank(message = "Отзыв не может быть пустой")
    private String content;
    @NotNull
    private Boolean isPositive;
    @NotNull
    private long userId;
    @NotNull
    private long filmId;
    private int useful;
}