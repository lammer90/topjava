package ru.javawebinar.topjava.controller;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class VirtualStorage implements Storage{
    private AtomicInteger idM = new AtomicInteger(0);
    private static final ConcurrentHashMap<Integer, Meal> MEALS = new ConcurrentHashMap<>();

    public VirtualStorage() {
        List<Meal> meals = Arrays.asList(
                new Meal(idM.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new Meal(idM.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new Meal(idM.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal(idM.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new Meal(idM.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new Meal(idM.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        meals.forEach(s -> MEALS.put(s.getId(), s));
    }

    @Override
    public void add(LocalDateTime dateTime, String description, int calories) {
        MEALS.put(idM.incrementAndGet(), new Meal(idM.get(), dateTime, description, calories));
    }

    @Override
    public void delete(int id) {
        MEALS.remove(id);
    }

    @Override
    public void update(Meal meal) {
        MEALS.put(meal.getId(), meal);
    }

    @Override
    public List<MealWithExceed> getAll() {
        return MealsUtil.getFilteredWithExceeded(new ArrayList<>(MEALS.values()), LocalTime.of(0, 0, 0), LocalTime.of(23, 59, 59), 2000);
    }

    @Override
    public Meal get(int id) {
        return MEALS.get(id);
    }
}
