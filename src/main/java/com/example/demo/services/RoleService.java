package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.Role;
import com.example.demo.repositories.RoleRepository;

@Service
public class RoleService {

	@Autowired
    private RoleRepository roleRepository;
	
	public List<Role> getAllRoles() {
        return roleRepository.findAllNotDeleted();
    }

    public Optional<Role> getRoleById(long id) {
        return roleRepository.findById(id);
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }
}
