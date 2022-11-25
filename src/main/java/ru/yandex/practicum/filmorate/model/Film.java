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
import java.util.LinkedHashSet;
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
    private Long duration;

    @JsonIgnore
    private Set<Long> usersId = new HashSet<>();

    @JsonIgnore
    private long rate = 0;

    private Mpa mpa;

    private Set<Genre> genres = new LinkedHashSet<>();

    public Film(@NotNull Long id, String name, String description, LocalDate releaseDate, Long duration, Mpa mpa) {
        super(id);
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }

//    public Film(@NotNull Long id, String name, String description, LocalDate releaseDate, Long duration, Long rate, Mpa mpa) {
//        super(id);
//        this.name = name;
//        this.description = description;
//        this.releaseDate = releaseDate;
//        this.duration = duration;
//        this.rate = rate;
//        this.mpa = mpa;
//    }

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
    public void addGenre(Genre genre) {
        genres.add(genre);
    }

    public void removeGenre(Genre genre) {
        genres.remove(genre);
    }

}
