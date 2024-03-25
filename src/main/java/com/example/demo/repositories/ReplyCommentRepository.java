package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.ReplyComment;

public interface ReplyCommentRepository extends JpaRepository<ReplyComment, Long>{
	
}
