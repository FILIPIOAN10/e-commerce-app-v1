package com.example.sb_ecom_v1.repository;

import com.example.sb_ecom_v1.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,Long> {

    Student findByFirstName(String name);
}
