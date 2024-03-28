package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.models.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{

//	@Query("SELECT s FROM Notification s WHERE s.notifyId = :notifyId and s.isDeleted = false")
//    List<Notification> findByNotifyId(@Param("notifyId") long notifyId);
	@Query("SELECT s FROM Comment s WHERE s.isDeleted = false")
	List<Comment> findAllNotDeleted();
	@Query("SELECT s FROM Comment s WHERE s.notify.notifyId = :notifyId and s.isDeleted = false")
    List<Comment> findByNotifyId(@Param("notifyId") long notifyId);
}
