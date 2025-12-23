package com.example.notification.repository;

import com.example.notification.model.NotificationLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationLogRepository extends JpaRepository<NotificationLogEntity, Long> {
    
    @Query("SELECT n FROM NotificationLogEntity n ORDER BY n.sentAt DESC")
    Page<NotificationLogEntity> findAllOrderBySentAtDesc(Pageable pageable);
    
}
