package com.example.demo.models;

import java.time.LocalDate;

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
@Table(name="Comment")
public class Comment {
	
	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long commentId;
	
	private String content;
	
	@ManyToOne
    @JoinColumn(name = "accountId") // Specify the foreign key column
    private Account account;
	
	@ManyToOne
    @JoinColumn(name = "notifyId") // Specify the foreign key column
    private Notification notify;
	
	private LocalDate dateCreated;
	
	private LocalDate lastModified;
	
	@Column(name = "isDeleted", columnDefinition = "boolean default false")
	private boolean isDeleted;
	
	
	private int likeNumber;
	

	public long getCommentId() {
		return commentId;
	}

	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Notification getNotify() {
		return notify;
	}

	public void setNotify(Notification notify) {
		this.notify = notify;
	}

	public LocalDate getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDate dateCreated) {
		this.dateCreated = dateCreated;
	}

	public LocalDate getLastModified() {
		return lastModified;
	}

	public void setLastModified(LocalDate lastModified) {
		this.lastModified = lastModified;
	}
	
	
	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
	

	public int getLikeNumber() {
		return likeNumber;
	}

	public void setLikeNumber(int likeNumber) {
		this.likeNumber = likeNumber;
	}

	public Comment() {
		
	}

	public Comment(long commentId, String content, Account account, Notification notify, LocalDate dateCreated,
			LocalDate lastModified, boolean isDeleted) {
		this.commentId = commentId;
		this.content = content;
		this.account = account;
		this.notify = notify;
		this.dateCreated = dateCreated;
		this.lastModified = lastModified;
		this.isDeleted = isDeleted;
	}

	public Comment(long commentId, String content, Account account, Notification notify, LocalDate dateCreated,
			LocalDate lastModified, boolean isDeleted, int likeNumber) {
		this.commentId = commentId;
		this.content = content;
		this.account = account;
		this.notify = notify;
		this.dateCreated = dateCreated;
		this.lastModified = lastModified;
		this.isDeleted = isDeleted;
		this.likeNumber = likeNumber;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
