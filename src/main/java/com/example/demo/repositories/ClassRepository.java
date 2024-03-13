package com.example.demo.repositories;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.models.Classes;
import com.example.demo.models.Faculty;


public interface ClassRepository extends JpaRepository<Classes, String>{
	
	@Query("SELECT s FROM Classes s WHERE s.classId = :classId and s.isDeleted = false")
	Optional<Classes> findByYourId(String classId);
	
	@Query("SELECT s FROM Classes s WHERE s.isDeleted = false")
    List<Classes> findAllNotDeleted();
	
	

}
