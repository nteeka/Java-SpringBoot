package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.models.SubmitHomework;

public interface SubmitHomeworkRepository extends JpaRepository<SubmitHomework, Long>{

	
	@Query("SELECT s.homework.homeworkId FROM SubmitHomework s WHERE s.account.accountId = :accountId")
    List<Long> listHomeworkByAccountId(@Param("accountId") long accountId);
}
