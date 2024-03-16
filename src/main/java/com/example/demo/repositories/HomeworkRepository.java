package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.models.Classes;
import com.example.demo.models.Homework;

public interface HomeworkRepository extends JpaRepository<Homework, Long> {
	
	@Query("SELECT s FROM Homework s WHERE s.classes.classId = :classId and s.isDeleted = false")
	List<Homework> findByClassId(@Param("classId") String classId);
}
