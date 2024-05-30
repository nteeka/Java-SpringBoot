package com.example.demo.models;

import org.apache.commons.lang3.RandomStringUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="QuizQuestion")
public class QuizQuestion {
	
	@Id
	private String quizAnswerId;
	
	private String question;
	
	private String fakeAnswer1;
	
	private String fakeAnswer2;
	
	private String fakeAnswer3;
	
	private String answer;
	
	@Column(name = "isDeleted", columnDefinition = "boolean default false")
	private boolean isDeleted;
	
	@ManyToOne
    @JoinColumn(name = "quizId")
    private Quiz quiz;

	public String getQuizAnswerId() {
		return quizAnswerId;
	}

	public void setQuizAnswerId(String quizAnswerId) {
		this.quizAnswerId = quizAnswerId;
	}
	
	

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	

	public String getFakeAnswer1() {
		return fakeAnswer1;
	}

	public void setFakeAnswer1(String fakeAnswer1) {
		this.fakeAnswer1 = fakeAnswer1;
	}

	public String getFakeAnswer2() {
		return fakeAnswer2;
	}

	public void setFakeAnswer2(String fakeAnswer2) {
		this.fakeAnswer2 = fakeAnswer2;
	}

	public String getFakeAnswer3() {
		return fakeAnswer3;
	}

	public void setFakeAnswer3(String fakeAnswer3) {
		this.fakeAnswer3 = fakeAnswer3;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Quiz getQuiz() {
		return quiz;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}
	
	public QuizQuestion() {		
		this.quizAnswerId = RandomStringUtils.randomAlphanumeric(8);			
	}

	
	public QuizQuestion(String question, String fakeAnswer1, String fakeAnswer2, String fakeAnswer3, String answer,
			boolean isDeleted, Quiz quiz) {
		this.question = question;
		this.fakeAnswer1 = fakeAnswer1;
		this.fakeAnswer2 = fakeAnswer2;
		this.fakeAnswer3 = fakeAnswer3;
		this.answer = answer;
		this.isDeleted = isDeleted;
		this.quiz = quiz;
	}
}
