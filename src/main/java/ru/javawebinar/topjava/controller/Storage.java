package ru.javawebinar.topjava.controller;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;

import java.time.LocalDateTime;
import java.util.List;

public interface Storage {
    void add(LocalDateTime dateTime, String description, int calories);
    void update(Meal meal);
    void delete(int id);
    List<MealWithExceed> getAll();
    Meal get(int id);
}
