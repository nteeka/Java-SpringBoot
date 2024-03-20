package com.example.demo.models;

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
@Table(name="CommentLike")
public class CommentLike {
	
	@Id
	@Column(name = "commentLikeId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long commentLikeId;
	
	@ManyToOne
    @JoinColumn(name = "commentId") // Specify the foreign key column
    private Comment comment;
	
	
	@ManyToOne
    @JoinColumn(name = "accountId") // Specify the foreign key column
    private Account account;
	
	
	@Column(name = "isDeleted", columnDefinition = "boolean default false")
	private boolean isDeleted;


	public long getCommentLikeId() {
		return commentLikeId;
	}


	public void setCommentLikeId(long commentLikeId) {
		this.commentLikeId = commentLikeId;
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


	public boolean isDeleted() {
		return isDeleted;
	}


	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	public CommentLike() {
		
	}

	public CommentLike(long commentLikeId, Comment comment, Account account, boolean isDeleted) {
		this.commentLikeId = commentLikeId;
		this.comment = comment;
		this.account = account;
		this.isDeleted = isDeleted;
	}
	
	
	
	
}
