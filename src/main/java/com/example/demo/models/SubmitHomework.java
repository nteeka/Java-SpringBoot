package com.example.demo.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="SubmitHomework")
public class SubmitHomework {
	
	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long submitHomeworkId;
	
	@ManyToOne
    @JoinColumn(name = "accountId") // Specify the foreign key column
    private Account account;
	
	@ManyToOne
    @JoinColumn(name = "homeworkId")
    private Homework homework;
	
	@Column(columnDefinition = "MEDIUMTEXT")
	private String description;
	
	private LocalDateTime dateSubmited;
	
	private LocalDateTime lastModified;
	
	private List<Long> filePath;
	
//	private String message;
	
	//đã nộp - nộp muộn
	@Column(name = "status", columnDefinition = "boolean default true")
	private boolean status;

	public long getSubmitHomeworkId() {
		return submitHomeworkId;
	}

	public void setSubmitHomeworkId(long submitHomeworkId) {
		this.submitHomeworkId = submitHomeworkId;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Homework getHomework() {
		return homework;
	}

	public void setHomework(Homework homework) {
		this.homework = homework;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Long> getFilePath() {
		return filePath;
	}

	public void setFilePath(List<Long> filePath) {
		this.filePath = filePath;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	
	
	public LocalDateTime getDateSubmited() {
		return dateSubmited;
	}

	public void setDateSubmited(LocalDateTime dateSubmited) {
		this.dateSubmited = dateSubmited;
	}

	public LocalDateTime getLastModified() {
		return lastModified;
	}

	public void setLastModified(LocalDateTime lastModified) {
		this.lastModified = lastModified;
	}

	public SubmitHomework() {
		
		
	}
	
	public SubmitHomework(long submitHomeworkId, Account account, Homework homework, String description,
			List<Long> filePath, boolean status) {
		
		this.submitHomeworkId = submitHomeworkId;
		this.account = account;
		this.homework = homework;
		this.description = description;
		this.filePath = filePath;
		this.status = status;
	}
	
	
	

}
