package com.example.sb_ecom_v1.service;

import com.example.sb_ecom_v1.model.Student;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudentService {
    Student findById(Long studentId);

    Student createStudent(Student student);

    List<Student> getAllStudents();
}
