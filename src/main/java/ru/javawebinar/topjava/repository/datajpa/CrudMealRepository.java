package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Meal m WHERE m.id=:id AND m.user.id=:user_id")
    int deleteById(@Param("id")int id, @Param("user_id")int userId);

    @Transactional
    @Override
    <S extends Meal> S save(S s);

    @Override
    Optional<Meal> findById(Integer integer);

    @Override
    Meal getOne(Integer integer);

    @Modifying
    @Query("SELECT m FROM Meal m WHERE m.user.id=:user_id ORDER BY m.dateTime DESC")
    List<Meal> getAll(@Param("user_id")int userId);

    @Modifying
    @Query("SELECT m FROM Meal m left join fetch m.user WHERE m.id=:id AND m.user.id=:user_id")
    List<Meal> getWihtUser(@Param("id")int id, @Param("user_id")int userId);

    Meal getByIdAndUser_Id(int id, int userId);

    int deleteByIdAndUser_Id(int id, int userId);
}
