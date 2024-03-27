package com.example.demo.models;

import java.time.LocalDate;
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
	
	private String description;
	
	private LocalDate dateSubmited;
	
	private LocalDate lastModified;
	
	private List<String> filePath;
	
//	private String message;
	
	@Column(name = "submited", columnDefinition = "boolean default false")
	private boolean submited;

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

	public List<String> getFilePath() {
		return filePath;
	}

	public void setFilePath(List<String> filePath) {
		this.filePath = filePath;
	}

	public boolean isSubmited() {
		return submited;
	}

	public void setSubmited(boolean submited) {
		this.submited = submited;
	}
	
	
	
	public LocalDate getDateSubmited() {
		return dateSubmited;
	}

	public void setDateSubmited(LocalDate dateSubmited) {
		this.dateSubmited = dateSubmited;
	}

	public LocalDate getLastModified() {
		return lastModified;
	}

	public void setLastModified(LocalDate lastModified) {
		this.lastModified = lastModified;
	}

	public SubmitHomework() {
		
		
	}
	
	public SubmitHomework(long submitHomeworkId, Account account, Homework homework, String description,
			List<String> filePath, boolean submited) {
		
		this.submitHomeworkId = submitHomeworkId;
		this.account = account;
		this.homework = homework;
		this.description = description;
		this.filePath = filePath;
		this.submited = submited;
	}
	
	
	

}
