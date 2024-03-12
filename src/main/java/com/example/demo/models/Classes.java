package com.example.demo.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="Class")
public class Classes {
	
//	
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private long classId;
	
	@Id
	private String classId;
	
	private String className;
	
	@Column(name = "isDeleted", columnDefinition = "boolean default false")
	private boolean isDeleted;
	
	
	@OneToMany(mappedBy = "classes")
    private List<Student> students = new ArrayList<>();
	
	@ManyToOne
    @JoinColumn(name = "accountId")
    private Account account;
	
	
	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
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
	
	
	
	
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Classes() {
		this.classId = RandomStringUtils.randomAlphanumeric(8);
	}
//	public Classes(long classId, String className, boolean isDeleted) {
//		this.classId = classId;
//		this.className = className;
//		this.isDeleted = isDeleted;
//	}

	public Classes(String classId, String className, List<Student> students) {
		this.classId = RandomStringUtils.randomAlphanumeric(8);
		this.className = className;
		this.students = students;
	}

	public Classes(String classId, String className, List<Student> students, Account account) {
		
		this.classId = classId;
		this.className = className;
		this.students = students;
		this.account = account;
}
	
	
	
	
}
