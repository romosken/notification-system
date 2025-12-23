package com.example.notification.repository;

import com.example.notification.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT DISTINCT u FROM UserEntity u " +
           "JOIN FETCH u.channels " +
           "JOIN FETCH u.subscribedCategories sc " +
           "WHERE sc.name = :categoryName")
    List<UserEntity> findByCategory(@Param("categoryName") String categoryName);
}
