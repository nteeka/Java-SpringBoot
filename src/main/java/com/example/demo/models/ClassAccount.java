package com.example.demo.models;

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
@Table(name="ClassAccount")
public class ClassAccount {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    Long classAccountId;

    @ManyToOne
    @JoinColumn(name = "accountId")
    Account account;

    @ManyToOne
    @JoinColumn(name = "classId")
    Classes classes;

    int num;

	public Long getClassAccountId() {
		return classAccountId;
	}

	public void setClassAccountId(Long classAccountId) {
		this.classAccountId = classAccountId;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Classes getClasses() {
		return classes;
	}

	public void setClasses(Classes classes) {
		this.classes = classes;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	public ClassAccount() {
		
		
	}
	public ClassAccount(Long classAccountId, Account account, Classes classes, int num) {
		
		this.classAccountId = classAccountId;
		this.account = account;
		this.classes = classes;
		this.num = num;
	}
    
}
