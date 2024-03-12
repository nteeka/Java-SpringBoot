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
@Table(name="Teacher")
public class Teacher {
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long teacher_id;
	
	@Column(name = "NAME")
	private String teacher_name;
	
	@Column(name = "isDeleted", columnDefinition = "boolean default false")
	private boolean isDeleted;
	
	
//    private String image;
    
//    private String email;


	public long getTeacher_id() {
		return teacher_id;
	}


	public void setTeacher_id(long teacher_id) {
		this.teacher_id = teacher_id;
	}


	public String getTeacher_name() {
		return teacher_name;
	}


	public void setTeacher_name(String teacher_name) {
		this.teacher_name = teacher_name;
	}


	public boolean isDeleted() {
		return isDeleted;
	}


	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}


//	public String getEmail() {
//		return email;
//	}
//
//
//	public void setEmail(String email) {
//		this.email = email;
//	}
	
	public Teacher() {		
	}

//	public Teacher(String teacher_name, String email) {		
//		this.teacher_name = teacher_name;
//		this.email = email;
//	}
	public Teacher(String teacher_name) {		
		this.teacher_name = teacher_name;
	}
    
	
    
  
	

    
   
	

	
	
	
	
	
}