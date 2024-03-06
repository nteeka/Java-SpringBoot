package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
	@Query("SELECT s FROM Role s WHERE s.isDeleted = false")
    List<Role> findAllNotDeleted();
}
