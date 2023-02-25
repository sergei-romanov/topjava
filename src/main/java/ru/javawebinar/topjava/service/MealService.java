package ru.javawebinar.topjava.service;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

import static ru.javawebinar.topjava.util.DateTimeUtil.isBetweenHalfOpen;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal, int userId) {
        return repository.save(userId, meal);
    }

    public void delete(int id, int userId) {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public Meal get(int id, int userId) {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    public List<Meal> getAll(int userid) {
        return repository.getAll(userid);
    }

    public List<Meal> getFilterList(int userId, @Nullable LocalDate startDate, @Nullable LocalDate endDate) {
        return repository.getAll(userId, meal -> isBetweenHalfOpen(meal.getDate(),
                startDate != null ? startDate : LocalDate.MIN,
                endDate != null ? endDate.plusDays(1) : LocalDate.MAX));
    }

    public Meal update(Meal meal, int userId) {
        return checkNotFoundWithId(repository.save(userId, meal), meal.getId());
    }
}
