package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;

@Controller
public class JspMealController {

    @Autowired
    private MealService mealService;


    @GetMapping("/meals")
    public String meals(Model model) {
        List<MealTo> mealTo = MealsUtil.getWithExcess(mealService.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay());
        model.addAttribute("meals", mealTo);
        return "meals";
    }

    @PostMapping("/meals")
    public String saveMeal(Model model, HttpServletRequest request) {
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (request.getParameter("id").isEmpty()) {
            mealService.create(meal, SecurityUtil.authUserId());
        } else {
            assureIdConsistent(meal, Integer.parseInt(request.getParameter("id")));
            mealService.update(meal, SecurityUtil.authUserId());
        }
        return "redirect:meals";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        return "mealForm";
    }

    @GetMapping("/update")
    public String update(Model model, HttpServletRequest request) {
        int id = Integer.valueOf(request.getParameter("id"));
        model.addAttribute("meal", mealService.get(id, SecurityUtil.authUserId()));
        return "mealForm";
    }

    @GetMapping("/delete")
    public String delete(HttpServletRequest request) {
        int id = Integer.valueOf(request.getParameter("id"));
        mealService.delete(id, SecurityUtil.authUserId());
        return "redirect:meals";
    }
}
