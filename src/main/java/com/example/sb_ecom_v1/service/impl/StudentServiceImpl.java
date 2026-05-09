package com.example.sb_ecom_v1.service.impl;

import com.example.sb_ecom_v1.exceptions.APIException;
import com.example.sb_ecom_v1.exceptions.ResourceNotFoundException;
import com.example.sb_ecom_v1.model.Student;
import com.example.sb_ecom_v1.repository.StudentRepository;
import com.example.sb_ecom_v1.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {

    private StudentRepository studentRepository;
    @Override
    public Student findById(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() ->new ResourceNotFoundException("Student", "studentId", studentId));
    }

    @Override
    public Student createStudent(Student student) {
        Student savedStudent= studentRepository.findByFirstName(student.getFirstName());
        if(savedStudent !=null){
            throw new APIException("Student with this name "+student.getFirstName()+ "already exists !!!");
        }
        return studentRepository.save(student);  // <-- trebuie return
    }

    @Override
    public List<Student> getAllStudents() {
        List<Student> stundents = studentRepository.findAll();
        if(stundents.isEmpty()){
            throw new APIException("No students created till now");
        }
        return studentRepository.findAll();
    }

}
