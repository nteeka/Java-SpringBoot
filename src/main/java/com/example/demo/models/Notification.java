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
@Table(name="Notification")
public class Notification {
	
	
	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long notifyId;
	
	private String title;
	
	@ManyToOne
    @JoinColumn(name = "classId") // Specify the foreign key column
    private Classes classes;
	
	@ManyToOne
    @JoinColumn(name = "accountId") // Specify the foreign key column
    private Account account;
	
	private String content;
	
	@Column(name = "isDeleted", columnDefinition = "boolean default false")
	private boolean isDeleted;
	
	private List<Long> filePath; // multiple file
	
	private LocalDateTime dateCreated;
	
	private LocalDateTime lastModifed;
	
	@Column(name = "numComment", columnDefinition = "bigint default 0")
    private long numComment;
	
	private boolean isPinned;
	
	public long getNotifyId() {
		return notifyId;
	}

	public void setNotifyId(long notifyId) {
		this.notifyId = notifyId;
	}
	
	
	
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Classes getClasses() {
		return classes;
	}

	public void setClasses(Classes classes) {
		this.classes = classes;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public List<Long> getFilePath() {
		return filePath;
	}

	public void setFilePath(List<Long> filePath) {
		this.filePath = filePath;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	
	public LocalDateTime getLastModifed() {
		return lastModifed;
	}

	public void setLastModifed(LocalDateTime lastModifed) {
		this.lastModifed = lastModifed;
	}
	
	

	public long getNumComment() {
		return numComment;
	}

	public void setNumComment(long numComment) {
		this.numComment = numComment;
	}
	
	

	public boolean getIsPinned() {
		return isPinned;
	}

	public void setIsPinned(boolean isPinned) {
		this.isPinned = isPinned;
	}

	public Notification() {		
		
	}



	public Notification(long notifyId, String title, Classes classes, Account account, String content,
			boolean isDeleted, List<Long> filePath, LocalDateTime dateCreated, LocalDateTime lastModifed,
			long numComment, boolean isPinned) {
		this.notifyId = notifyId;
		this.title = title;
		this.classes = classes;
		this.account = account;
		this.content = content;
		this.isDeleted = isDeleted;
		this.filePath = filePath;
		this.dateCreated = dateCreated;
		this.lastModifed = lastModifed;
		this.numComment = numComment;
		this.isPinned = isPinned;
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
