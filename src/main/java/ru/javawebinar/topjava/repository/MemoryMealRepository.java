package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.isNull;

public class MemoryMealRepository implements MealRepository {
    private final AtomicInteger id = new AtomicInteger(1);

    private final ConcurrentMap<Integer, Meal> storage = new ConcurrentHashMap<>();

    public MemoryMealRepository() {
        List<Meal> meals = Arrays.asList(
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
        meals.forEach(this::save);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Meal get(int id) {
        return storage.get(id);
    }

    @Override
    public Meal save(Meal meal) {
        return isNull(meal.getId()) ? create(meal) : update(meal);
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }

    private Meal create(Meal meal) {
        int newId = generateId();
        Meal newMeal = new Meal(newId, meal.getDateTime(), meal.getDescription(), meal.getCalories());
        storage.put(newId, newMeal);
        return newMeal;
    }

    private Meal update(Meal meal) {
        if (!storage.containsKey(meal.getId())) {
            return null;
        }
        storage.put(meal.getId(), meal);
        return meal;
    }

    private int generateId() {
        return id.incrementAndGet();
    }
}
