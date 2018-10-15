package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.controller.Storage;
import ru.javawebinar.topjava.controller.VirtualStorage;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

public class MealServlet extends HttpServlet {
    private Storage storage = new VirtualStorage();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        String date = request.getParameter("date");
        String desc = request.getParameter("desc");
        String cal = request.getParameter("cal");
        if (id != null) {
            if (id.equals("-1")) {
                storage.add(LocalDateTime.parse(date), desc, Integer.parseInt(cal));
            } else {
                storage.update(new Meal(Integer.parseInt(id), LocalDateTime.parse(date), desc, Integer.parseInt(cal)));
            }
        }
        response.sendRedirect("meals");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String del = request.getParameter("del");
        if (id != null) {
            if (del != null) {
                storage.delete(Integer.parseInt(id));
                response.sendRedirect("meals");

            } else {
                if (!id.equals("-1")) {
                    request.setAttribute("meal", storage.get(Integer.parseInt(id)));
                }
                request.setAttribute("id", Integer.parseInt(id));
                request.getRequestDispatcher("jsp/edit.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("meals", storage.getAll());
            request.getRequestDispatcher("jsp/meals.jsp").forward(request, response);
        }
    }
}
