package com.example.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="Subject")
public class Subject {
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long subject_id;
	
	@Column(name = "NAME")
	private String subject_name;
	
	
	@Column(name = "isDeleted", columnDefinition = "boolean default false")
	private boolean isDeleted;


	public long getSubject_id() {
		return subject_id;
	}


	public void setSubject_id(long subject_id) {
		this.subject_id = subject_id;
	}


	public String getSubject_name() {
		return subject_name;
	}


	public void setSubject_name(String subject_name) {
		this.subject_name = subject_name;
	}


	public boolean isDeleted() {
		return isDeleted;
	}


	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	public Subject() {
		
	}

	public Subject(String subject_name) {
		this.subject_name = subject_name;
	}
	
	
}
