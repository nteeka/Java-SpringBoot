package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.models.SubmitHomework;
import com.example.demo.models.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {
	@Query("SELECT ca FROM Todo ca WHERE ca.account.accountId = :accountId")
    List<Todo> listTodoByAccountId(@Param("accountId") Long accountId);
}
