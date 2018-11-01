package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.Util;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class JdbcMealRepositoryImpl implements MealRepository {

    private static final BeanPropertyRowMapper<Meal> MEAL_BEAN_PROPERTY_ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcMealRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("meals")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Meal save(Meal meal, int userId) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("date_time", meal.getDateTime())
                .addValue("description", meal.getDescription())
                .addValue("calories", meal.getCalories());

        if (meal.isNew()) {
            int newId = simpleJdbcInsert.execute(map);
            meal.setId(newId);
            return meal;
        }
        else {
            map.addValue("id", meal.getId());
            int result = namedParameterJdbcTemplate.update("UPDATE meals SET date_time=:date_time, description=:description, calories=:calories WHERE user_id=:user_id AND id=:id", map);
            return result == 0 ? null : meal;
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        return jdbcTemplate.update("DELETE FROM meals WHERE user_id=? AND id=?", userId, id) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return jdbcTemplate.query("SELECT * FROM meals WHERE user_id=? AND id=?", MEAL_BEAN_PROPERTY_ROW_MAPPER, userId, id).stream().findFirst().orElse(null);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getAllSorted(jdbcTemplate.query("SELECT * FROM meals WHERE user_id=?", MEAL_BEAN_PROPERTY_ROW_MAPPER, userId), meal -> true);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return getAllSorted(jdbcTemplate.query("SELECT * FROM meals WHERE user_id=?", MEAL_BEAN_PROPERTY_ROW_MAPPER, userId), meal -> Util.isBetween(meal.getDate(), startDate.toLocalDate(), endDate.toLocalDate()));
    }

    private List<Meal> getAllSorted(List<Meal> meals, Predicate<Meal> predicate){
        return meals.stream().filter(predicate).sorted(Comparator.comparing(Meal::getDateTime).reversed()).collect(Collectors.toList());
    }
}
