package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.models.CommentLike;
import com.example.demo.models.Notification;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long>{
	
	@Query("SELECT s FROM CommentLike s WHERE s.account.accountId = :accountId and s.isDeleted = false")
    List<CommentLike> findByAccountId(@Param("accountId") long accountId);

}
