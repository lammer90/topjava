package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.MealServiceTest;


@ActiveProfiles({"postgres", "jdbc"})
public class JdbcMealRepositoryImplTest extends MealServiceTest {
}