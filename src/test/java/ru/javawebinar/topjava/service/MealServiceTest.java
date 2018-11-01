package ru.javawebinar.topjava.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.*;

@ContextConfiguration({"classpath*:spring/spring-app.xml", "classpath*:spring/spring-db.xml"})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService service;

    static {
        SLF4JBridgeHandler.install();
    }

    @Test
    public void get() {
        Meal meal = new Meal(100005, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000);
        Assertions.assertThat(service.get(100005, 100000)).isEqualTo(meal);
    }

    @Test(expected = NotFoundException.class)
    public void getForeign() {
        service.get(100005, 100001);
    }

    @Test(expected = NotFoundException.class)
    public void deleteCreate() {
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY, 31, 11, 0), "Завтрак", 1000);
        service.create(meal, 100000);
        Assert.assertEquals(service.getAll(100000).size(), 7);
        service.delete(100010, 100000);
        Assert.assertEquals(service.getAll(100000).size(), 6);
        service.get(100010, 100000);
    }

    @Test
    public void getBetweenDateTimes() {
        Assert.assertEquals(service.getAll(100000).size(), 6);
    }

    @Test
    public void getAll() {
        Assert.assertEquals(service.getBetweenDateTimes(
                 LocalDateTime.of(2015, Month.MAY, 31, 0, 0)
                ,LocalDateTime.of(2015, Month.MAY, 31, 23, 59)
                , 100000).size(), 3);
    }

    @Test
    public void update() {
        Meal meal = new Meal(100005, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Теперь обед", 1000);
        service.update(meal, 100000);
        Assertions.assertThat(service.get(100005, 100000)).isEqualTo(meal);
    }

    @Test(expected = NotFoundException.class)
    public void updateForeign() {
        Meal meal = new Meal(100005, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Теперь обед", 1000);
        service.update(meal, 100001);
    }

}