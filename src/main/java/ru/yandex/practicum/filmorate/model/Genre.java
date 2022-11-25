package ru.yandex.practicum.filmorate.model;

import lombok.*;

@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Genre extends BaseModel {

    String name;

}
