package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.models.QuizQuestion;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, String> {
	
	@Query("SELECT s FROM QuizQuestion s WHERE s.quiz.quizId = :quizId and s.quiz.isDeleted = false and  s.isDeleted = false")
	List<QuizQuestion> findByQuizId(@Param("quizId") String quizId);
}
