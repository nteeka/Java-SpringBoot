package com.example.demo.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.DetailTeaching;


public interface DetailTeachingRepository extends JpaRepository<DetailTeaching, Long> {
//	@Query("SELECT s FROM DetailTeaching s WHERE s. = false")
//    List<Student> findAllNotDeleted();
}
