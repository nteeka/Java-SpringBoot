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
	

	
    private String image;
    
    private String email;
    
	
    @ManyToOne
    @JoinColumn(name = "facultyId") // Specify the foreign key column
    private Faculty faculty;
	
    @ManyToOne
    @JoinColumn(name = "classId") // Specify the foreign key column
    private Classes classes;

    
    
	public Classes getClasses() {
		return classes;
	}
	public void setClasses(Classes classes) {
		this.classes = classes;
	}
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

	public Faculty getFaculty() {
		return faculty;
	}
	public void setFaculty(Faculty faculty) {
		this.faculty = faculty;
	}
	
	
	
	
	

	public Student(String name, int age, String image, String email, Faculty faculty) {
		this.name = name;
		this.age = age;
		this.image = image;
		this.email = email;
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
	public Student(String name, int age, String image, String email, Faculty faculty, Classes classes) {

		this.name = name;
		this.age = age;
		this.image = image;
		this.email = email;
		this.faculty = faculty;
		this.classes = classes;
	}
	

	
	
	
	
	
}