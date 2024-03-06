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

import java.time.LocalDateTime;


@Entity
@Data
@Table(name="Student")
public class Student {
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "AGE")
	private int age;
	
	@Column(name = "isDeleted", columnDefinition = "boolean default false")
	private boolean isDeleted;
	
//	@Column(nullable = true, length = 64)
//	private String image;
	
    private String image;
    
    private String email;
    
//    private String password;
	
    @ManyToOne
    @JoinColumn(name = "facultyId") // Specify the foreign key column
    private Faculty faculty;
	
	
    @Column(name = "resetToken")
    private String resetToken;
    
    @Column(name = "resetTokenExpiration")
    private LocalDateTime resetTokenExpiration;
    
    
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
//	public String getPassword() {
//		return password;
//	}
//	public void setPassword(String password) {
//		this.password = password;
//	}
	public Faculty getFaculty() {
		return faculty;
	}
	public void setFaculty(Faculty faculty) {
		this.faculty = faculty;
	}
	
	
	
	
	public String getResetToken() {
		return resetToken;
	}
	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}
	
	public boolean isResetTokenExpired(LocalDateTime check) {
        return LocalDateTime.now().isAfter(check);
    }
	
	
	
	public LocalDateTime getResetTokenExpiration() {
		return resetTokenExpiration;
	}
	public void setResetTokenExpiration(LocalDateTime resetTokenExpiration) {
		this.resetTokenExpiration = resetTokenExpiration;
	}
	public Student(String name, int age, String image, String email, Faculty faculty) {
		this.name = name;
		this.age = age;
		this.image = image;
		this.email = email;
		this.faculty = faculty;
	}
	public Student(String name, int age, String image, Faculty faculty) {
		this.name = name;
		this.age = age;
		this.image = image;
		this.faculty = faculty;
	}
	public Student(String name, int age) {
		
		this.name = name;
		this.age = age;
	}
	public Student() {
		
	}
	public Student(String name, int age, String image) {
		this.name = name;
		this.age = age;
		this.image = image;
	}
	

	
	
	
	
	
}