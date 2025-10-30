package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {  //TODO: Переписать валидацию на Spring Validator
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
