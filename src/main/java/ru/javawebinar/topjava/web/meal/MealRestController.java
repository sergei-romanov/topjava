package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.getFilteredTos;
import static ru.javawebinar.topjava.util.MealsUtil.getTos;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private static final Logger log = getLogger(MealRestController.class);
    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        log.debug("get all");
        return getTos(service.getAll(authUserId(), meal -> true), authUserCaloriesPerDay());
    }

    public List<MealTo> getFilterList(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        log.debug("get filter list");
        List<Meal> filterList = service.getFilterList(authUserId(), startDate, endDate);
        return getFilteredTos(filterList, authUserCaloriesPerDay(), startTime, endTime);
    }

    public Meal get(int id) {
        log.debug("get meal id={}", id);
        return service.get(id, authUserId());
    }

    public Meal create(Meal meal) {
        checkNew(meal);
        log.debug("create meal {}", meal.getDescription());
        return service.create(meal, authUserId());
    }

    public void update(Meal meal, int id) {
        log.debug("update meal id={}", meal.getId());
        assureIdConsistent(meal, id);
        service.update(meal, authUserId());
    }

    public void delete(int id) {
        log.debug("delete meal id={}", id);
        service.delete(id, authUserId());
    }
}
