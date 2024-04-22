package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.models.CommentLike;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long>{
	
	@Query("SELECT s FROM CommentLike s WHERE s.account.accountId = :accountId AND s.account.isDeleted = false")
    List<CommentLike> findByAccountId(@Param("accountId") long accountId);
	
	@Query("SELECT s FROM CommentLike s WHERE s.account.accountId = :accountId AND s.account.isDeleted = false and s.comment.commentId = :commentId AND s.comment.isDeleted = false")
    Optional<CommentLike> checkLiked(@Param("accountId") long accountId, @Param("commentId") long commentId);

}
