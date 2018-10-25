package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MealServiceImpl implements MealService {

    @Autowired
    private MealRepository repository;

    @Override
    public Meal create(Meal meal, int userId) {
        return ValidationUtil.checkNotFound(repository.save(meal, userId), "Not our Id");
    }

    @Override
    public void delete(int id, int userId) throws NotFoundException {
        ValidationUtil.checkNotFoundWithId(repository.delete(id, userId), id);
    }

    @Override
    public Meal get(int id, int userId) throws NotFoundException {
        return ValidationUtil.checkNotFoundWithId(repository.get(id, userId), id);
    }

    @Override
    public void update(Meal meal, int userId) {
        ValidationUtil.checkNotFound(repository.save(meal, userId), "Not our Id");
    }

    @Override
    public List<Meal> getAll(int userId, LocalDate ldStart, LocalDate ldEnd) {
        return new ArrayList<>(repository.getAll(userId, ldStart, ldEnd));
    }
}