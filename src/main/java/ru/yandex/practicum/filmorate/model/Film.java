package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {  //TODO: Переписать валидацию на Spring Validator
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
}
