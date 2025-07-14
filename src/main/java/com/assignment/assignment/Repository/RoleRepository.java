package com.assignment.assignment.Repository;

import com.assignment.assignment.models.ERole;
import com.assignment.assignment.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);

    boolean existsByName(ERole roleEnum);
}
