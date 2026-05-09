package com.example.sb_ecom_v1.controller;


import com.example.sb_ecom_v1.model.Student;
import com.example.sb_ecom_v1.service.StudentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class StudentController {

    private StudentService studentService;
    @GetMapping("/public/student/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long studentId){

        Student student = studentService.findById(studentId);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/public/student")
    public ResponseEntity<List<Student>> getAllStudents(){
        List<Student> students = studentService.getAllStudents();
        return new  ResponseEntity<>(students,HttpStatus.OK);
    }

    @PostMapping("/public/student")
    public ResponseEntity<Student> createStudent(@Valid @RequestBody Student student){
        Student createdStudent = studentService.createStudent(student);
        return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
    }
}
