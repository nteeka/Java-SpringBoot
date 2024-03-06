package com.example.demo.models;


import java.time.LocalDateTime;

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
@Table(name="Account")
public class Account {
	
	@Id
	@Column(name = "accountId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long accountId;
	
	
	
    private String image;
    
    private String email;
    
    @Column(name = "isDeleted", columnDefinition = "boolean default false")
	private boolean isDeleted;
    
    private String password;
	
    @ManyToOne
    @JoinColumn(name = "roleId") // Specify the foreign key column
    private Role role;
    
    @Column(name = "resetToken")
    private String resetToken;
    
    @Column(name = "resetTokenExpiration")
    private LocalDateTime resetTokenExpiration;

    
    
	public String getResetToken() {
		return resetToken;
	}

	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}

	public LocalDateTime getResetTokenExpiration() {
		return resetTokenExpiration;
	}

	public void setResetTokenExpiration(LocalDateTime resetTokenExpiration) {
		this.resetTokenExpiration = resetTokenExpiration;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Account(String image, String email, String password, Role role) {		
		this.image = image;
		this.email = email;
		this.password = password;
		this.role = role;
	}
    
	public Account() {		
		
	}
	
	public boolean isResetTokenExpired(LocalDateTime check) {
        return LocalDateTime.now().isAfter(check);
    }
    
    
	
	
    
}
