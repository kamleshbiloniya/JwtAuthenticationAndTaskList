package com.assignment.assignment.config;

import com.assignment.assignment.Repository.RoleRepository;
import com.assignment.assignment.models.ERole;
import com.assignment.assignment.models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializer implements CommandLineRunner{
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        for (ERole role : ERole.values()) {
            if (!roleRepository.findByName(role).isPresent()) {
                roleRepository.save(new Role(role));
                System.out.println("Inserted role: " + role.name());
            }
        }
    }
}
