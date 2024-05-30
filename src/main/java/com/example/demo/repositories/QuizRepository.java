package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.models.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, String>{
	
	@Query("SELECT s FROM Quiz s WHERE s.classes.classId = :classId and s.classes.isDeleted = false and s.isDeleted = false")
	List<Quiz> findByClassId(@Param("classId") String classId);
	

}
