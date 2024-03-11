package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.models.DetailTeaching;
import com.example.demo.models.Student;


public interface DetailTeachingRepository extends JpaRepository<DetailTeaching, Long> {
//	@Query("SELECT s FROM DetailTeaching s WHERE s. = false")
//    List<Student> findAllNotDeleted();
}
