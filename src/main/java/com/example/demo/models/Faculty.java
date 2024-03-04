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
@Table(name="Faculty")
public class Faculty {
	@Id
	@Column(name = "facultyId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long facultyId;
	
	@Column(name = "NAME")
	private String facultyName;
	
	@Column(name = "isDeleted", columnDefinition = "boolean default false")
	private boolean isDeleted;

	public long getFacultyId() {
		return facultyId;
	}

	public void setFacultyId(long facultyId) {
		this.facultyId = facultyId;
	}

	public String getFacultyName() {
		return facultyName;
	}

	public void setFacultyName(String facultyName) {
		this.facultyName = facultyName;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Faculty(String facultyName, boolean isDeleted) {		
		this.facultyName = facultyName;
		this.isDeleted = isDeleted;
	}
	
	public Faculty() {
		
	}
	
	
	
	
}
