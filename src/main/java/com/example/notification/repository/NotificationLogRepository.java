package com.example.notification.repository;

import com.example.notification.model.NotificationLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationLogRepository extends JpaRepository<NotificationLogEntity, Long> {
}
