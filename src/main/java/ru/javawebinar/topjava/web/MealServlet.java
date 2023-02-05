package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.Names;
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

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static final Logger log = getLogger(MealServlet.class);
    private Repository repository;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        repository = new MemoryRepositoryImpl(MealsUtil.getMemoryData());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (Names.CREATE.equals(action)) {
            request.setAttribute("meal", new Meal(MemoryRepositoryImpl.generateId(), LocalDateTime.now(), null, 0));
            request.getRequestDispatcher("create.jsp").forward(request, response);
            return;
        }
        if (Names.UPDATE.equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            Meal meal = repository.get(id);
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("create.jsp").forward(request, response);
            return;
        }
        if (Names.DELETE.equals(action)) {
            String id = request.getParameter("id");
            repository.delete(Integer.parseInt(id));
            response.sendRedirect("meals");
            log.info(String.format("Delete id = %s", id));
            return;
        }
        log.info("getList");
        request.setAttribute("meals", MealsUtil.filteredByStreams(repository.getAll(), LocalTime.of(0, 0),
                LocalTime.of(23, 59, 59), Names.CALORIES_PER_DAY));
        request.getRequestDispatcher("meals.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        int id = Integer.parseInt(request.getParameter("id"));
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String descriptions = request.getParameter("descriptions");
        int calories = Integer.parseInt(request.getParameter("calories"));
        repository.save(new Meal(id == 0 ? null : id, dateTime, descriptions, calories));
        response.sendRedirect("meals");
    }

}
