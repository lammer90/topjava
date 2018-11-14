package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.UserServiceTest;

@ActiveProfiles({"postgres", "jdbc"})
public class JdbcUserRepositoryImplTest extends UserServiceTest {
}