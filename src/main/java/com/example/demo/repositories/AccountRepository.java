package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.models.Account;
import com.example.demo.models.Student;

public interface AccountRepository extends JpaRepository<Account, Long>{
	@Query("SELECT s FROM Account s WHERE s.isDeleted = false")
    List<Account> findAllNotDeleted();
	
	@Query("SELECT s FROM Account s WHERE s.email = :email AND s.isDeleted = false")
    Optional<Account> findByEmail(@Param("email") String email);
	
	@Query("SELECT s FROM Account s WHERE s.email = :email AND s.accountId != :currentAccountId AND s.isDeleted = false")
	Optional<Account> findByEmailAndIdNot(@Param("email") String email, @Param("currentAccountId") long currentAccountId);

	
	@Query("SELECT s FROM Account s WHERE s.resetToken = :resetToken")
    Optional<Account> findByResetToken(@Param("resetToken") String resetToken);

}
