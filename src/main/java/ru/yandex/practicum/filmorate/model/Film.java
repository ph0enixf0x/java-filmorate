package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class Film {  //TODO: Переписать валидацию на Spring Validator
    private int id;
    @NotBlank
    private String name;
    @Max(value = 200)
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private Set<Integer> likes;
}
