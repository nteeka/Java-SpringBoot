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
@Table(name="DetailTeaching")
public class DetailTeaching {
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long detailTeach_id;

	
	@Column(name = "isDeleted", columnDefinition = "boolean default false")
	private boolean isDeleted;
	
	
    @ManyToOne
    @JoinColumn(name = "teacher_id") // Specify the foreign key column
    private Teacher teacher;
	
    @ManyToOne
    @JoinColumn(name = "subject_id") // Specify the foreign key column
    private Subject subject;
    
    @ManyToOne
    @JoinColumn(name = "classId") // Specify the foreign key column
    private Classes classes;
    
    
    
}
