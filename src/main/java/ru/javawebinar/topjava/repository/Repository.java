package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface Repository {

    List<Meal> getListMeal();

    Meal getMeal(int id);

    Meal saveMeal(Meal meal);

    void deleteMeal(int id);

}
