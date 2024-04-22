package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.models.ClassAccount;

public interface ClassAccountRepository extends JpaRepository<ClassAccount, Long> {

	@Query("SELECT s FROM ClassAccount s WHERE s.classes.classId = :classId AND  s.classes.isDeleted = false")
    List<ClassAccount> findByClassId(@Param("classId") String classId);
	
	@Query("SELECT s FROM ClassAccount s WHERE s.account.accountId = :accountId AND s.account.isDeleted = false")
    List<ClassAccount> findByAccountId(@Param("accountId") long accountId);
	
	@Query("SELECT ca FROM ClassAccount ca WHERE ca.classes.classId = :classId AND ca.classes.isDeleted = false AND ca.account.accountId = :accountId AND ca.account.isDeleted = false")
    ClassAccount findByClassIdAndAccountId(@Param("classId") String classId, @Param("accountId") long accountId);

	@Query("SELECT COUNT(ca) FROM ClassAccount ca WHERE ca.classes.classId = :classId AND ca.classes.isDeleted = false")
    Long countAccountsInClass(String classId);

}







