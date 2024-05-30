package com.example.demo.models;

import java.time.LocalDateTime;

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
@Table(name="ReplyComment")
public class ReplyComment {
	
	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long replyCommentId;
	
	@ManyToOne
    @JoinColumn(name = "commentId") // Specify the foreign key column
    private Comment comment;
	
	@ManyToOne
    @JoinColumn(name = "accountId") // Specify the foreign key column
    private Account account;
	
	
	private LocalDateTime dateCreated;
	
	private LocalDateTime lastModified;
	
	@Column(name = "isDeleted", columnDefinition = "boolean default false")
	private boolean isDeleted;
	
	private String content;

	public long getReplyCommentId() {
		return replyCommentId;
	}

	public void setReplyCommentId(long replyCommentId) {
		this.replyCommentId = replyCommentId;
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}
	

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public LocalDateTime getLastModified() {
		return lastModified;
	}

	public void setLastModified(LocalDateTime lastModified) {
		this.lastModified = lastModified;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	
	public ReplyComment() {
	
	}

	public ReplyComment(long replyCommentId, Comment comment, Account account, LocalDateTime dateCreated,
			LocalDateTime lastModified, boolean isDeleted, String content) {

		this.replyCommentId = replyCommentId;
		this.comment = comment;
		this.account = account;
		this.dateCreated = dateCreated;
		this.lastModified = lastModified;
		this.isDeleted = isDeleted;
		this.content = content;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
