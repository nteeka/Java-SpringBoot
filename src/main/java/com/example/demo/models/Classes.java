package com.example.demo.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="Class")
public class Classes {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long classId;
	
	private String className;
	
	@Column(name = "isDeleted", columnDefinition = "boolean default false")
	private boolean isDeleted;
	
	
	@OneToMany(mappedBy = "classes")
    private List<Student> students = new ArrayList<>();
	
	
	
	
	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public long getClassId() {
		return classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public Classes() {
		
	}
//	public Classes(long classId, String className, boolean isDeleted) {
//		this.classId = classId;
//		this.className = className;
//		this.isDeleted = isDeleted;
//	}

	public Classes(long classId, String className, boolean isDeleted, List<Student> students) {
		this.classId = classId;
		this.className = className;
		this.isDeleted = isDeleted;
		this.students = students;
	}
	
	
	
	
}
