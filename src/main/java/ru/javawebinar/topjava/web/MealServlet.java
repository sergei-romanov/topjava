package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MemoryRepositoryImpl;
import ru.javawebinar.topjava.repository.Repository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static java.util.Objects.isNull;
import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private int CALORIES_PER_DAY = 2000;

    private final String DELETE = "delete";

    private final String UPDATE = "update";

    private final String CREATE = "create";

    private static final Logger log = getLogger(MealServlet.class);
    private Repository repository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        repository = new MemoryRepositoryImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String param = request.getParameter("action");
        String action = isNull(param) ? "" : param;
        switch (action) {
            case CREATE:
                Meal mealCreate = new Meal(0, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), null, 0);
                request.setAttribute("meal", mealCreate);
                request.getRequestDispatcher("mealsChange.jsp").forward(request, response);
                break;
            case UPDATE:
                Meal mealUpdate = repository.getMeal(Integer.parseInt(request.getParameter("id")));
                request.setAttribute("meal", mealUpdate);
                request.getRequestDispatcher("mealsChange.jsp").forward(request, response);
                break;
            case DELETE:
                String id = request.getParameter("id");
                repository.deleteMeal(Integer.parseInt(id));
                response.sendRedirect("meals");
                log.info("Delete id = {}", id);
                break;
            default:
                request.setAttribute("meals", MealsUtil.filteredByStreams(repository.getListMeal(), LocalTime.MIN,
                        LocalTime.MAX, CALORIES_PER_DAY));
                request.getRequestDispatcher("meals.jsp").forward(request, response);
                log.info("getList");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        int id = Integer.parseInt(request.getParameter("id"));
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String descriptions = request.getParameter("descriptions");
        int calories = Integer.parseInt(request.getParameter("calories"));
        Meal meal = repository.saveMeal(new Meal(id, dateTime, descriptions, calories));
        response.sendRedirect("meals");
        log.info("Change id = {}", meal.getId());
    }
}
