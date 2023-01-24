package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println(filteredByRecursive(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> result = new ArrayList<>();
        Map<LocalDate, Integer> caloriesSumByDate = new HashMap<>();
        meals.forEach(meal -> caloriesSumByDate.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum));
        meals.forEach(meal -> {
            LocalDateTime dateTimeMeals = meal.getDateTime();
            if (TimeUtil.isBetweenHalfOpen(dateTimeMeals.toLocalTime(), startTime, endTime)) {
                result.add(new UserMealWithExcess(dateTimeMeals, meal.getDescription(), meal.getCalories(),
                        caloriesSumByDate.get(dateTimeMeals.toLocalDate()) > caloriesPerDay));
            }
        });
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(Collectors.toMap(UserMeal::getDate, UserMeal::getCalories, Integer::sum));
        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByRecursive(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> result = new ArrayList<>();
        Map<LocalDate, Integer> mapDailyCalorie = new HashMap<>();
        filterRecursive(meals, startTime, endTime, caloriesPerDay, mapDailyCalorie, 0, result);
        return result;
    }

    private static void filterRecursive(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay,
                                        Map<LocalDate, Integer> mapDailyCalorie, int index, List<UserMealWithExcess> result) {
        if (index >= meals.size()) {
            return;
        }
        UserMeal userMeal = meals.get(index++);
        mapDailyCalorie.merge(userMeal.getDate(), userMeal.getCalories(), Integer::sum);
        filterRecursive(meals, startTime, endTime, caloriesPerDay, mapDailyCalorie, index, result);
        if (TimeUtil.isBetweenHalfOpen(userMeal.getTime(), startTime, endTime)) {
            result.add(createMealWithExcess(userMeal, mapDailyCalorie.get(userMeal.getDate()) > caloriesPerDay));
        }
    }

    private static UserMealWithExcess createMealWithExcess(UserMeal meal, boolean excess) {
        return new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
