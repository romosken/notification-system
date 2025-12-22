package com.example.notification.repository;

import com.example.notification.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM users u JOIN users_categories c ON u.id = c.userId WHERE c = :category")
    List<User> findBySubscribedCategory(@Param("category") String category);
}
