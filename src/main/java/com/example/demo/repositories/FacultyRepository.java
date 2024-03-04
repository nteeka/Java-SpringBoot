package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.models.Faculty;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
	@Query("SELECT s FROM Faculty s WHERE s.isDeleted = false")
    List<Faculty> findAllNotDeleted();
}
