package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.models.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
	
	@Query("SELECT s FROM Notification s WHERE s.classes.classId = :classId and s.classes.isDeleted = false and s.isDeleted = false")
	List<Notification> findByClassId(@Param("classId") String classId);
	
	@Query("SELECT s.notifyId FROM Notification s WHERE s.classes.classId = :classId and s.classes.isDeleted = false  AND s.isDeleted = false")
	List<Long> listNotifyId(@Param("classId") String classId);
	
	
	@Query("SELECT s FROM Notification s WHERE s.classes.classId = :classId and s.classes.isDeleted = false and s.isDeleted = false and s.isPinned = true")
	List<Notification> listPinnedNotiByClassId(@Param("classId") String classId);
}
