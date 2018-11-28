package ru.javawebinar.topjava.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.web.meal.MealRestController;

import java.lang.reflect.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final Logger log = LoggerFactory.getLogger(JdbcUserRepositoryImpl.class);

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private int batchQuery(List<Role> roles, int userId) {
        int[] ints = jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Role role = roles.get(i);
                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, role.name());
            }

            @Override
            public int getBatchSize() {
                return roles.size();
            }
        });

        for (int i = 0; i < ints.length; i++) {
            if (ints[i] == 0) {
                return 0;
            }
        }
        return 1;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        final List<Role> roles = new ArrayList<>(user.getRoles());
        /*log.debug("Transaction active:::: {}", TransactionSynchronizationManager.isActualTransactionActive());*/

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());

            batchQuery(roles, newKey.intValue());

        } else {
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
            if (namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0
                    || batchQuery(roles, user.getId()) == 0) {
                return null;
            }
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_roles r ON users.id = r.user_id WHERE id=?", ROW_MAPPER, id);
        User user = DataAccessUtils.singleResult(users);

        if (user == null) {
            return null;
        }

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM user_roles WHERE user_id=?", user.getId());
        Set<Role> roles = new HashSet<>();
        while (sqlRowSet.next()) {
            String roleStr = sqlRowSet.getString("role");
            roles.add(Role.valueOf(roleStr));
        }
        user.setRoles(roles);
        return user;
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_roles r ON users.id = r.user_id WHERE email=?", ROW_MAPPER, email);
        User user = DataAccessUtils.singleResult(users);

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM user_roles WHERE user_id=?", user.getId());
        Set<Role> roles = new HashSet<>();
        while (sqlRowSet.next()) {
            String roleStr = sqlRowSet.getString("role");
            roles.add(Role.valueOf(roleStr));
        }
        user.setRoles(roles);
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(AbstractBaseEntity::getId, o -> o));

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM user_roles ");
        while (sqlRowSet.next()) {
            int id = sqlRowSet.getInt("user_id");
            String roleStr = sqlRowSet.getString("role");
            if (userMap.get(id).getRoles() == null) {
                Set<Role> roles = new HashSet<>();
                roles.add(Role.valueOf(roleStr));
                userMap.get(id).setRoles(roles);
            } else {
                userMap.get(id).getRoles().add(Role.valueOf(roleStr));
            }
        }
        return users;
    }
}
