package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseModel {

    Long id;

}
