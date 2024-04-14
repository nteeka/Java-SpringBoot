package com.example.demo.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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
	
	
	private String description;
	
	
	private LocalDateTime dateCreated;
	
	private LocalDateTime lastModified;
	
	private LocalDate deadline; 
	
	@Column(name = "isDeleted", columnDefinition = "boolean default false")
	private boolean isDeleted;
	
	@Lob
	@Column(columnDefinition = "MEDIUMTEXT")
	private List<String> filePath;
	
	@Column(name = "status", columnDefinition = "boolean default false")
	private boolean status;
	
	

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public List<String> getFilePath() {
		return filePath;
	}

	public void setFilePath(List<String> filePath) {
		this.filePath = filePath;
	}

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

//	public Subject getSubject() {
//		return subject;
//	}
//
//	public void setSubject(Subject subject) {
//		this.subject = subject;
//	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

//	public int getSubmited() {
//		return submited;
//	}
//
//	public void setSubmited(int submited) {
//		this.submited = submited;
//	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
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
		this.deadline = deadline;
	}

//	public void setDeadline(LocalDate deadline) {
//		if (dateCreated != null && deadline != null && deadline.isBefore(dateCreated)) {
//            throw new IllegalArgumentException("Deadline must be after dateCreated.");
//        }
//		this.deadline = deadline;
//	}
	
	
	
	public LocalDateTime getLastModified() {
		return lastModified;
	}

	public void setLastModified(LocalDateTime lastModified) {
		this.lastModified = lastModified;
	}

//	@PrePersist
//    public void prePersist() {
//        if (deadline.isBefore(dateCreated)) {
//            throw new IllegalArgumentException("Deadline must be after dateCreated.");
//        }
//    }
//	@PrePersist
//    protected void onCreate() {
//        this.dateCreated = LocalDate.now();
//    }

	public Homework()
	{		
	}
	
	



	public Homework(long homeworkId, String homeworkName, Classes classes, String description, LocalDateTime dateCreated,
			LocalDateTime lastModified, LocalDate deadline, boolean isDeleted, List<String> filePath, boolean status) {
		this.homeworkId = homeworkId;
		this.homeworkName = homeworkName;
		this.classes = classes;
		this.description = description;
		this.dateCreated = dateCreated;
		this.lastModified = lastModified;
		this.deadline = deadline;
		this.isDeleted = isDeleted;
		this.filePath = filePath;
		this.status = status;
	}

	
	
	

	
	
	
	
	
}
