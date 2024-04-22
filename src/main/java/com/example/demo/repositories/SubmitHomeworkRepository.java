package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.models.SubmitHomework;

public interface SubmitHomeworkRepository extends JpaRepository<SubmitHomework, Long>{

	
	@Query("SELECT s.homework.homeworkId FROM SubmitHomework s WHERE s.account.accountId = :accountId and s.account.isDeleted = false")
    List<Long> listHomeworkByAccountId(@Param("accountId") long accountId);
	
	@Query("SELECT s FROM SubmitHomework s WHERE s.homework.homeworkId = :homeworkId and s.homework.isDeleted = false")
    List<SubmitHomework> listSubmitHomeworkByHomeworkId(@Param("homeworkId") long homeworkId);
	
	@Query("SELECT ca FROM SubmitHomework ca WHERE ca.homework.classes.classId = :classId and ca.homework.classes.isDeleted = false and ca.homework.isDeleted = false")
    List<SubmitHomework> countSubmited(String classId);
	
	@Query("SELECT s FROM SubmitHomework s WHERE s.homework.homeworkId = :homeworkId and s.homework.isDeleted = false")
    Optional<SubmitHomework> findByHomeworkId(@Param("homeworkId") Long homeworkId);
}
