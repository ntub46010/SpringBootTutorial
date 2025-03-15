package com.example.demo.controller;

import com.example.demo.model.entity.Contact;
import com.example.demo.model.entity.Department;
import com.example.demo.model.entity.Student;
import com.example.demo.model.request.StudentRequest;
import com.example.demo.model.response.StudentResponse;
import com.example.demo.repository.ContactRepository;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
public class MyController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @GetMapping("/students")
    public ResponseEntity<List<StudentResponse>> getStudents(
            @RequestParam(required = false, defaultValue = "") String name
    ) {
        var students = studentRepository.findByNameLikeIgnoreCase("%" + name + "%");
        var studentDepartmentMap = createStudentDepartmentMap(students);
        var studentContactMap = createStudentContactMap(students);

        var responses = students
                .stream()
                .map(s -> {
                    var department = studentDepartmentMap.get(s);
                    var contact = studentContactMap.get(s);
                    var res = new StudentResponse();
                    res.setId(s.getId());
                    res.setName(s.getName());
                    res.setDepartmentName(department.getName());
                    res.setEmail(contact.getEmail());
                    res.setPhone(contact.getPhone());

                    return res;
                })
                .toList();

        return ResponseEntity.ok(responses);
    }

    @PostMapping("/students")
    public ResponseEntity<Void> createStudent(@RequestBody StudentRequest request) {
        var departmentOp = departmentRepository.findById(request.getDepartmentId());
        if (departmentOp.isEmpty()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        var student = new Student();
        student.setName(request.getName());
        student.setContact(request.getContact());
        student.setDepartment(departmentOp.get());
        studentRepository.save(student);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/students/{id}/contact")
    public ResponseEntity<Void> updateStudentContact(
            @PathVariable Long id, @RequestBody Contact request
    ) {
        var studentOp = studentRepository.findById(id);
        if (studentOp.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var student = studentOp.get();
        var contact = student.getContact();
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        studentRepository.save(student);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/departments/{id}/students")
    public ResponseEntity<List<StudentResponse>> getStudentsByDepartment(@PathVariable Long id) {
        var departmentOp = departmentRepository.findById(id);
        if (departmentOp.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var department = departmentOp.get();
        var students = department.getStudents();
        var responses = students
                .stream()
                .map(s -> {
                    var res = new StudentResponse();
                    res.setId(s.getId());
                    res.setName(s.getName());

                    return res;
                })
                .toList();

        return ResponseEntity.ok(responses);
    }

    private Map<Student, Contact> createStudentContactMap(Collection<Student> students) {
        var studentContactIdMap = students
                .stream()
                .collect(Collectors.toMap(Function.identity(), s -> s.getContact().getId()));

        var contacts = contactRepository.findAllById(studentContactIdMap.values());
        var contactMap = contacts
                .stream()
                .collect(Collectors.toMap(Contact::getId, Function.identity()));

        var map = new HashMap<Student, Contact>();
        students.forEach(s -> {
            var contactId = studentContactIdMap.get(s);
            var contact = contactMap.get(contactId);
            map.put(s, contact);
        });

        return map;
    }

    private Map<Student, Department> createStudentDepartmentMap(Collection<Student> students) {
        var studentDepartmentIdMap = students
                .stream()
                .collect(Collectors.toMap(Function.identity(), s -> s.getDepartment().getId()));

        var departments = departmentRepository.findAllById(studentDepartmentIdMap.values());
        var departmentMap = departments
                .stream()
                .collect(Collectors.toMap(Department::getId, Function.identity()));

        var map = new HashMap<Student, Department>();
        students.forEach(s -> {
            var deptId = studentDepartmentIdMap.get(s);
            var dept = departmentMap.get(deptId);
            map.put(s, dept);
        });

        return map;
    }
}