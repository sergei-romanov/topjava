package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MemoryRepositoryImpl implements Repository {
    private static final AtomicInteger id = new AtomicInteger(1);
    private final ConcurrentMap<Integer, Meal> storage;

    public MemoryRepositoryImpl(List<Meal> storage) {
        this.storage = storage.stream().collect(Collectors.toConcurrentMap(Meal::getId, meal -> meal));
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Meal get(Integer id) {
        return storage.get(id);
    }

    @Override
    public void save(Meal meal) {
        storage.put(meal.getId(), meal);
    }

    @Override
    public void delete(Integer id) {
        storage.remove(id);
    }

    public static int generateId() {
        return id.incrementAndGet();
    }
}
