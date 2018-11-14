package ru.javawebinar.topjava.repository.jpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.MealServiceTest;

@ActiveProfiles({"postgres", "jpa"})
public class JpaMealRepositoryImplTest extends MealServiceTest {
}