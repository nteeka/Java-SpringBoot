package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.models.Classes;
import com.example.demo.models.FileAttach;

public interface FileAttachRepository extends JpaRepository<FileAttach, Long> {
	
	@Query("SELECT DISTINCT s.homework.homeworkId FROM FileAttach s")
	List<Long> findDistinctHomeworkIds();
	
	
	@Query("SELECT s.fileAttId FROM FileAttach s WHERE s.submitHomework.submitHomeworkId = :submitHomeworkId")
    List<Long> findIdBySubmitId(@Param("submitHomeworkId") Long submitHomeworkId);
	
	@Query("SELECT s FROM FileAttach s WHERE s.submitHomework.submitHomeworkId = :submitHomeworkId")
    List<FileAttach> findBySubmitId(@Param("submitHomeworkId") Long submitHomeworkId);
	
	@Query("SELECT s.fileAttId FROM FileAttach s WHERE s.homework.homeworkId = :homeworkId")
    List<Long> findIdByHomeworkId(@Param("homeworkId") Long homeworkId);
	
	@Query("SELECT s FROM FileAttach s WHERE s.homework.homeworkId = :homeworkId")
    List<FileAttach> findByHomeworkId(@Param("homeworkId") Long homeworkId);
	
	@Query("SELECT s.fileAttId FROM FileAttach s WHERE s.notify.notifyId = :notifyId")
    List<Long> findIdByNotifyId(@Param("notifyId") Long notifyId);
	
	@Query("SELECT s FROM FileAttach s WHERE s.notify.notifyId = :notifyId")
    List<FileAttach> findByNotifyId(@Param("notifyId") Long notifyId);

}
