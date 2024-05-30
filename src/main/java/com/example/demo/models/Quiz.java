package com.example.demo.models;

import java.time.LocalDateTime;
import java.util.HashMap;

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
@Table(name="Quiz")
public class Quiz {
	
	@Id
	private String quizId;
	
	private String quizName;
	
	@Column(name = "isDeleted", columnDefinition = "boolean default false")
	private boolean isDeleted;
	
	private String description ;
	
	private LocalDateTime dateCreated ;
	
	private LocalDateTime dateModified  ;
	
	@ManyToOne
    @JoinColumn(name = "classId")
    private Classes classes;
	
	private HashMap<Long, Integer> listMember   ;
	
	
	public String getQuizId() {
		return quizId;
	}

	public void setQuizId(String quizId) {
		this.quizId = quizId;
	}

	public String getQuizName() {
		return quizName;
	}

	public void setQuizName(String quizName) {
		this.quizName = quizName;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
	
	
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public LocalDateTime getDateModified() {
		return dateModified;
	}

	public void setDateModified(LocalDateTime dateModified) {
		this.dateModified = dateModified;
	}

	public Classes getClasses() {
		return classes;
	}

	public void setClasses(Classes classes) {
		this.classes = classes;
	}

	public HashMap<Long, Integer> getListMember() {
		return listMember;
	}

	public void setListMember(HashMap<Long, Integer> listMember) {
		this.listMember = listMember;
	}

	public Quiz() {	
		this.quizId = RandomStringUtils.randomAlphanumeric(8);;	
	}

	public Quiz(String quizName, boolean isDeleted, String description, LocalDateTime dateCreated,
			LocalDateTime dateModified, Classes classes, HashMap<Long, Integer> listMember) {
		
		this.quizId = RandomStringUtils.randomAlphanumeric(8);;	
		this.quizName = quizName;
		this.isDeleted = isDeleted;
		this.description = description;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
		this.classes = classes;
		this.listMember = listMember;
	}

	
	
	
}
