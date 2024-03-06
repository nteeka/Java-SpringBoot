package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.Account;
import com.example.demo.repositories.AccountRepository;

@Service
public class AccountService {

	@Autowired
    private AccountRepository accountRepository;
	
	
	
	public List<Account> getAllAccounts() {
        return accountRepository.findAllNotDeleted();
    }

    public Optional<Account> getAccountById(long id) {
        return accountRepository.findById(id);
    }

    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    public String deleteAccount(long id) {
    	Optional<Account> optionalAccount = accountRepository.findById(id);

        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setDeleted(true);
            accountRepository.save(account); // Save the changes back to the database
            return "redirect:/StudentView/listStudent";   
        }
        return "redirect:/StudentView/listStudent";   
        
    }
	
	
	
	
	
	public boolean isEmailTaken(String email) {
        return accountRepository.findByEmail(email).isPresent();
    }
    public boolean isEmailTaken(String email,long id) {
        return accountRepository.findByEmailAndIdNot(email,id).isPresent();
    }
    public boolean containsUppercaseAndLowercase(String str) {
        boolean hasUppercase = false;
        boolean hasLowercase = false;

        for (char c : str.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            }
        }

        return hasUppercase && hasLowercase;
    }
}
