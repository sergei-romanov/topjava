package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface Repository {

    List<Meal> getAll();

    Meal get(Integer id);

    void save(Meal meal);

    void delete(Integer id);

}
