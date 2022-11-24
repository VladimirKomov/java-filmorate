package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.BaseModel;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

public abstract class AbstractService<T extends BaseModel> {

    private long generateId = 0L;

    Storage<T> storage;

    public T create(T data) {
        validate(data);
        data.setId(++generateId);
        storage.create(data);
        return data;
    }

    protected abstract void validate(T data);

    public T get(long id) {
        return storage.get(id);
    }

    public T update(T data) {
        storage.get(data.getId());
        data.setId(data.getId());
        validate(data);
        storage.update(data);
        return data;
    }

    public void delete(long id) {
        storage.delete(id);
    }

    public void deleteAll() {
        storage.deleteAll();
    }

    public List<T> getAll() {
        return storage.getAll();
    }

}
