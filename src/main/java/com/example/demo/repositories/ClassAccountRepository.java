package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.models.ClassAccount;
import com.example.demo.models.Student;

public interface ClassAccountRepository extends JpaRepository<ClassAccount, Long> {

	@Query("SELECT s FROM ClassAccount s WHERE s.classes.classId = :classId")
    List<ClassAccount> findByClassId(@Param("classId") String classId);
	
	@Query("SELECT s FROM ClassAccount s WHERE s.account.accountId = :accountId")
    List<ClassAccount> findByAccountId(@Param("accountId") long accountId);
}
