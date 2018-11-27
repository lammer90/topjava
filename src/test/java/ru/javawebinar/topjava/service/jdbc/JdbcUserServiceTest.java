package ru.javawebinar.topjava.service.jdbc;

import org.junit.Assume;
import org.junit.AssumptionViolatedException;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.Profiles.JDBC;

@ActiveProfiles(JDBC)
public class JdbcUserServiceTest extends AbstractUserServiceTest {
}