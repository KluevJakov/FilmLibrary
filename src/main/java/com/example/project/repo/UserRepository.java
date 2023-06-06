package com.example.project.repo;

import com.example.project.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByLogin(String login);

    @Query(value = "select distinct * from users inner join users_roles on users.id = users_roles.user_id " +
            "inner join roles on roles.id = users_roles.roles_id where roles.id = 1 " +
            "and LOWER(email) like CONCAT('%', :search, '%') or LOWER(login) like CONCAT('%', :search, '%') limit 20", nativeQuery = true)
    List<User> findUsersSearch(@Param("search") String search);

    @Query(value = "select distinct * from users inner join users_roles on users.id = users_roles.user_id " +
            "inner join roles on roles.id = users_roles.roles_id where roles.id = 1 limit 20", nativeQuery = true)
    List<User> findUsers();

    @Query(value = "select * from users where email = ?1 limit 1", nativeQuery = true)
    User findByEmail(String email);

}
