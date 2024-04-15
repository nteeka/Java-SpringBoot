package com.example.demo.models;

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
@Table(name="FileAttach")
public class FileAttach {
	
	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long fileAttId;
	
	
	@ManyToOne
    @JoinColumn(name = "notifyId") // Specify the foreign key column
    private Notification notify;
	
	@ManyToOne
    @JoinColumn(name = "homeworkId") // Specify the foreign key column
    private Homework homework;
	
	@ManyToOne
    @JoinColumn(name = "submitHomeworkId") // Specify the foreign key column
    private SubmitHomework submitHomework;
	
	private String filePath;

	public long getFileAttId() {
		return fileAttId;
	}

	public void setFileAttId(long fileAttId) {
		this.fileAttId = fileAttId;
	}

	public Notification getNotify() {
		return notify;
	}

	public void setNotify(Notification notify) {
		this.notify = notify;
	}

	public Homework getHomework() {
		return homework;
	}

	public void setHomework(Homework homework) {
		this.homework = homework;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	

	public SubmitHomework getSubmitHomework() {
		return submitHomework;
	}

	public void setSubmitHomework(SubmitHomework submitHomework) {
		this.submitHomework = submitHomework;
	}

	public FileAttach() {
		
	}
	
	
	public FileAttach(long fileAttId, Notification notify, Homework homework, SubmitHomework submitHomework,
			String filePath) {
		this.fileAttId = fileAttId;
		this.notify = notify;
		this.homework = homework;
		this.submitHomework = submitHomework;
		this.filePath = filePath;
	}

	
	
	
}
