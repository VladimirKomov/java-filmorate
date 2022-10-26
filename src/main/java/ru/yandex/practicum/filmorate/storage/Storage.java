package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.BaseModel;

import java.util.List;

public interface Storage<T extends BaseModel> {

    void create(T data);

    void update(T data);

    T get(long id);

    void delete(long id);

    void deleteAll();

    List<T> getAll();

    int getSize();

}
