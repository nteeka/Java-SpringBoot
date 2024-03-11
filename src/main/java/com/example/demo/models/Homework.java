package com.example.demo.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="Homework")
public class Homework {
	
	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long homeworkId;
	
	private String homeworkName;
	
	@ManyToOne
    @JoinColumn(name = "classId") // Specify the foreign key column
    private Classes classes;
	
	@ManyToOne
    @JoinColumn(name = "subject_id") // Specify the foreign key column
    private Subject subject;
	
	private String description;
	
	private int submited = 0; // sl da nop homework cua hs mot class
	
	private LocalDate dateCreated;  
	
	private LocalDate deadline; 
	
	@Column(name = "isDeleted", columnDefinition = "boolean default false")
	private boolean isDeleted;

	public long getHomeworkId() {
		return homeworkId;
	}

	public void setHomeworkId(long homeworkId) {
		this.homeworkId = homeworkId;
	}

	public String getHomeworkName() {
		return homeworkName;
	}

	public void setHomeworkName(String homeworkName) {
		this.homeworkName = homeworkName;
	}

	public Classes getClasses() {
		return classes;
	}

	public void setClasses(Classes classes) {
		this.classes = classes;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getSubmited() {
		return submited;
	}

	public void setSubmited(int submited) {
		this.submited = submited;
	}

	public LocalDate getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDate dateCreated) {
		this.dateCreated = dateCreated;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	public LocalDate getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDate deadline) {
		if (dateCreated != null && deadline != null && deadline.isBefore(dateCreated)) {
            throw new IllegalArgumentException("Deadline must be after dateCreated.");
        }
		this.deadline = deadline;
	}
	@PrePersist
    public void prePersist() {
        if (deadline.isBefore(dateCreated)) {
            throw new IllegalArgumentException("Deadline must be after dateCreated.");
        }
    }

	public Homework()
	{		
	}
	
	public Homework(long homeworkId, String homeworkName, Classes classes, Subject subject, String description,
			LocalDate deadline) {
		this.homeworkId = homeworkId;
		this.homeworkName = homeworkName;
		this.classes = classes;
		this.subject = subject;
		this.description = description;
		this.deadline = deadline;
	}

	
	
	
	
	
}
