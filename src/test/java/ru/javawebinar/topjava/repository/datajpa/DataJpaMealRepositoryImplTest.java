package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.MealServiceTest;

@ActiveProfiles({"postgres", "datajpa"})
public class DataJpaMealRepositoryImplTest extends MealServiceTest {
}