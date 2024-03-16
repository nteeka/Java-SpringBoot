package com.example.demo.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
	
	
	@Id
	private String classId;
	
	private String className;
	
	private String description;
	
	@Column(name = "isDeleted", columnDefinition = "boolean default false")
	private boolean isDeleted;
	
	
	
		
	@OneToMany(mappedBy = "classes")
    Set<ClassAccount> classAccount;
	
	@ManyToOne
    @JoinColumn(name = "accountId")
    private Account account;
	
	//description
	//subject(maybe) - category
	//
	
	
//	public List<Account> getAccounts() {
//		return accounts;
//	}
//
//	public void setAccounts(List<Account> accounts) {
//		this.accounts = accounts;
//	}
	
	public Set<ClassAccount> getClassAccount() {
		return classAccount;
	}

	public void setClassAccount(Set<ClassAccount> classAccount) {
		this.classAccount = classAccount;
	}

//	public List<Student> getStudents() {
//		return students;
//	}
//
//	public void setStudents(List<Student> students) {
//		this.students = students;
//	}

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
	
	
	
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Classes(String classId, String className) {
		this.classId = RandomStringUtils.randomAlphanumeric(8);
		this.className = className;
	}

	public Classes(String classId, String className, Account account) {
		
		this.classId = classId;
		this.className = className;
		this.account = account;
}

	public Classes(String className, boolean isDeleted, Account account,
			Set<ClassAccount> classAccount) {
		
		
		this.className = className;
		this.isDeleted = isDeleted;
		this.account = account;
		this.classAccount = classAccount;
	}

	public Classes(String className, String description,
			Set<ClassAccount> classAccount, Account account) {
		
		this.className = className;
		this.description = description;
		this.classAccount = classAccount;
		this.account = account;
	}
	

//	public Classes(String classId, String className, List<Student> students, List<Account> accounts, Account account) {
//		
//		this.classId = classId;
//		this.className = className;
//		this.students = students;
//		this.accounts = accounts;
//		this.account = account;
//	}
	
	
	
	
	
}
