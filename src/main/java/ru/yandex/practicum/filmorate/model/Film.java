package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.yandex.practicum.filmorate.validation.AfterFirstFilm;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static ru.yandex.practicum.filmorate.Constants.MAX_FILM_DESCRIPTION_SIZE;

@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Film extends BaseModel {

    @NotBlank
    private String name;
    @Size(max = MAX_FILM_DESCRIPTION_SIZE)
    private String description;
    @AfterFirstFilm
    private LocalDate releaseDate;
    @Positive
    private int duration;

    @JsonIgnore
    private Set<Long> usersId = new HashSet<>();

    @JsonIgnore
    private long rate = 0;

    public Film(@NotNull Long id, String name, String description, LocalDate releaseDate, int duration) {
        super(id);
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return duration == film.duration && Objects.equals(name, film.name)
                && Objects.equals(description, film.description)
                && Objects.equals(releaseDate, film.releaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, releaseDate, duration);
    }

    public void addLike(long userId) {
        usersId.add(userId);
        updateRate();
    }

    public void removeLike(long userId) {
        usersId.remove(userId);
        updateRate();
    }

    private void updateRate() {
        rate = usersId.size();
    }
}
