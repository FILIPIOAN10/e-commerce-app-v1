package com.example.sb_ecom_v1.repository;

import com.example.sb_ecom_v1.model.AppRole;
import com.example.sb_ecom_v1.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}
