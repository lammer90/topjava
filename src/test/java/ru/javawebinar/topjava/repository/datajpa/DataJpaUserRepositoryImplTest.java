package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.UserServiceTest;

@ActiveProfiles({"postgres", "datajpa"})
public class DataJpaUserRepositoryImplTest extends UserServiceTest {
}