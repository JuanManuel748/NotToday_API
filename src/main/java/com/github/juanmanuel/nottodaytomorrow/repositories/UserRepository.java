package com.github.juanmanuel.nottodaytomorrow.repositories;

import com.github.juanmanuel.nottodaytomorrow.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(
            value = "SELECT * FROM users AS u WHERE u.name LIKE %?1%",
            nativeQuery = true
    )
    List<User> findByName(String name);
    @Query(
            value = "SELECT * FROM users AS u WHERE u.email = ?1",
            nativeQuery = true
    )
    Optional<User> findByEmail(String email);

}
