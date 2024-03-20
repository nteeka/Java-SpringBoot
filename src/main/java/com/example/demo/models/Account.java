package com.example.demo.models;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name="Account")
public class Account {
	
	@Id
	@Column(name = "accountId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long accountId;
	
	
	private String displayName;
	
	private String bio;
	
    private String image;  
    
    private String email;
    
    private LocalDate timeCreated;
    
    private String password; 
    
    @Column(name = "isDeleted", columnDefinition = "boolean default false")
	private boolean isDeleted;
    
    @Column(name = "resetToken")
    private String resetToken;
    
    @Column(name = "resetTokenExpiration")
    private LocalDateTime resetTokenExpiration;
    
	
    @ManyToOne
    @JoinColumn(name = "roleId") // Specify the foreign key column
    private Role role;
    

    @OneToMany(mappedBy = "account")
    Set<ClassAccount> classAccount;
    
    
//  @ManyToOne
//  @JoinColumn(name = "classId") // Specify the foreign key column
//  private List<Classes> classes = new ArrayList<>();
//  @OneToMany(mappedBy = "classId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.)
//  private List<Classes> classes = new ArrayList<>();
  
//  @ManyToOne
//  @JoinColumn(name = "classId") // Specify the foreign key column
//  private Classes classes;

    
    
	public String getResetToken() {
		return resetToken;
	}

//	public List<Classes> getClasses() {
//		return classes;
//	}
//
//	public void setClasses(List<Classes> classes) {
//		this.classes = classes;
//	}

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
	
	
	
    
	

public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public LocalDate getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(LocalDate timeCreated) {
		this.timeCreated = timeCreated;
	}

public Account(long accountId, String image, String email, String password, boolean isDeleted, String resetToken,
			LocalDateTime resetTokenExpiration, Role role, Set<ClassAccount> classAccount) {
		
		this.accountId = accountId;
		this.image = image;
		this.email = email;
		this.password = password;
		this.isDeleted = isDeleted;
		this.resetToken = resetToken;
		this.resetTokenExpiration = resetTokenExpiration;
		this.role = role;
		this.classAccount = classAccount;
	}

//	public Account(long accountId, String image, String email, String password, boolean isDeleted, String resetToken,
//			LocalDateTime resetTokenExpiration, Role role, List<Classes> classes) {
//		
//		this.accountId = accountId;
//		this.image = image;
//		this.email = email;
//		this.password = password;
//		this.isDeleted = isDeleted;
//		this.resetToken = resetToken;
//		this.resetTokenExpiration = resetTokenExpiration;
//		this.role = role;
//		this.classes = classes;
//	}

	public Set<ClassAccount> getClassAccount() {
		return classAccount;
	}

	public void setClassAccount(Set<ClassAccount> classAccount) {
		this.classAccount = classAccount;
	}

	public Account() {		
		
	}
	
	public boolean isResetTokenExpired(LocalDateTime check) {
        return LocalDateTime.now().isAfter(check);
    }

	public Account(long accountId, String displayName, String bio, String image, String email,
			LocalDate timeCreated, String password, boolean isDeleted, String resetToken,
			LocalDateTime resetTokenExpiration, Role role, Set<ClassAccount> classAccount) {
		this.accountId = accountId;
		this.displayName = displayName;
		this.bio = bio;
		this.image = image;
		this.email = email;
		this.timeCreated = timeCreated;
		this.password = password;
		this.isDeleted = isDeleted;
		this.resetToken = resetToken;
		this.resetTokenExpiration = resetTokenExpiration;
		this.role = role;
		this.classAccount = classAccount;
	}
    
    
	
	
    
}
