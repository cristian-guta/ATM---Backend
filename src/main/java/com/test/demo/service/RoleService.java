package com.test.demo.service;

import com.test.demo.model.Role;
import com.test.demo.repository.RoleRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void seedRoles() {
        seedRole("Admin role", "ADMIN");
        seedRole("User role", "USER");
    }

    private void seedRole(String description, String roleName) {
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            role = new Role().setDescription(description).setName(roleName);
            roleRepository.save(role);
        }
    }
}