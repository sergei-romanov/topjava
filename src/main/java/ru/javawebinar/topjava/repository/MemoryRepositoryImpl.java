package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MemoryRepositoryImpl implements Repository {
    private static final AtomicInteger id = new AtomicInteger(1);
    private final ConcurrentMap<Integer, Meal> storage;

    public MemoryRepositoryImpl() {
        List<Meal> meals = new ArrayList<>();
        meals.add(new Meal(generateId(), LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        meals.add(new Meal(generateId(), LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        meals.add(new Meal(generateId(), LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        meals.add(new Meal(generateId(), LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        meals.add(new Meal(generateId(), LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        meals.add(new Meal(generateId(), LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        meals.add(new Meal(generateId(), LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
        this.storage = meals.stream().collect(Collectors.toConcurrentMap(Meal::getId, meal -> meal));
    }

    @Override
    public List<Meal> getListMeal() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Meal getMeal(int id) {
        return storage.get(id);
    }

    @Override
    public Meal saveMeal(Meal meal) {
        return meal.getId() == 0 ? save(meal) : update(meal);
    }

    @Override
    public void deleteMeal(int id) {
        storage.remove(id);
    }

    private Meal save(Meal meal) {
        int uuid = generateId();
        Meal result = new Meal(uuid, meal.getDateTime(), meal.getDescription(), meal.getCalories());
        storage.put(uuid, result);
        return result;
    }

    private Meal update(Meal meal) {
        storage.put(meal.getId(), meal);
        return meal;
    }

    private static int generateId() {
        return id.incrementAndGet();
    }
}
