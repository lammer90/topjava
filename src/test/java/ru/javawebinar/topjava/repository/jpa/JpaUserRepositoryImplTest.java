package ru.javawebinar.topjava.repository.jpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.UserServiceTest;

@ActiveProfiles({"postgres", "jpa"})
public class JpaUserRepositoryImplTest extends UserServiceTest {

}