package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.models.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
	@Query("SELECT s FROM Student s WHERE s.isDeleted = false")
    List<Student> findAllNotDeleted();
	
	@Query("SELECT s FROM Student s WHERE s.email = :email AND s.isDeleted = false")
    Optional<Student> findByEmail(@Param("email") String email);
	
	@Query("SELECT s FROM Student s WHERE s.email = :email AND s.id != :currentStudentId AND s.isDeleted = false")
	Optional<Student> findByEmailAndIdNot(@Param("email") String email, @Param("currentStudentId") long currentStudentId);

	
	@Query("SELECT s FROM Student s WHERE s.resetToken = :resetToken")
    Optional<Student> findByResetToken(@Param("resetToken") String resetToken);
	
	
}
