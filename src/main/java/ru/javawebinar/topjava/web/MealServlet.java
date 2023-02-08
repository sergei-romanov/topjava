package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MemoryMealRepository;
import ru.javawebinar.topjava.repository.MealRepository;
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
    private static final int CALORIES_PER_DAY = 2000;

    private static final String DELETE = "delete";

    private static final String UPDATE = "update";

    private static final String CREATE = "create";

    private static final Logger log = getLogger(MealServlet.class);
    private MealRepository mealRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        mealRepository = new MemoryMealRepository();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String param = request.getParameter("action");
        String action = isNull(param) ? "" : param;
        switch (action) {
            case CREATE:
                Meal mealCreate = new Meal(null, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), null, 0);
                request.setAttribute("meal", mealCreate);
                request.getRequestDispatcher("mealsChange.jsp").forward(request, response);
                break;
            case UPDATE:
                Meal mealUpdate = mealRepository.get(Integer.parseInt(request.getParameter("id")));
                request.setAttribute("meal", mealUpdate);
                request.getRequestDispatcher("mealsChange.jsp").forward(request, response);
                break;
            case DELETE:
                String id = request.getParameter("id");
                mealRepository.delete(Integer.parseInt(id));
                response.sendRedirect("meals");
                log.info("Delete id = {}", id);
                break;
            default:
                request.setAttribute("meals", MealsUtil.filteredByStreams(mealRepository.getAll(), LocalTime.MIN,
                        LocalTime.MAX, CALORIES_PER_DAY));
                request.getRequestDispatcher("meals.jsp").forward(request, response);
                log.info("getList");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String descriptions = request.getParameter("descriptions");
        int calories = Integer.parseInt(request.getParameter("calories"));
        Meal meal = mealRepository.save(new Meal(id.isEmpty() ? null : Integer.parseInt(id), dateTime, descriptions, calories));
        response.sendRedirect("meals");
        log.info("save id = {}", meal.getId());
    }
}
