package com.example.demo.controller;

import com.example.demo.entity.Student;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RestController
public class MyController {

    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("/students")
    public ResponseEntity<Void> createStudent(@RequestBody Student student) {
        student.setId(null);
        studentRepository.save(student);

        var uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .build(Map.of("id", student.getId()));

        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        var studentOp = studentRepository.findById(id);
        return studentOp.isPresent()
                ? ResponseEntity.ok(studentOp.get())
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/students/ids")
    public ResponseEntity<List<Student>> getStudents(@RequestParam List<Long> idList) {
        var students = studentRepository.findAllById(idList);
        return ResponseEntity.ok(students);
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<Void> updateStudent(
            @PathVariable Long id, @RequestBody Student request
    ) {
        var studentOp = studentRepository.findById(id);
        if (studentOp.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var student = studentOp.get();
        student.setName(request.getName());
        student.setGrade(request.getGrade());
        student.setBloodType(request.getBloodType());
        student.setBirthday(request.getBirthday());
        student.setContact(request.getContact());
        studentRepository.save(student);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/students")
    public ResponseEntity<List<Student>> getStudents(
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortDirection,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        var sort = createSort(sortField, sortDirection);
        var pageable = createPageable(page, size, sort);
        var students = studentRepository.find(pageable);

        return ResponseEntity.ok(students);
    }

    private Sort createSort(String field, String direction) {
        if (field == null && direction == null) {
            return Sort.unsorted();
        }

        if (field == null ^ direction == null) {
            return Sort.unsorted();
        }

        Sort.Order order;
        if ("asc".equalsIgnoreCase(direction)) {
            order = Sort.Order.asc(field);
        } else if ("desc".equalsIgnoreCase(direction)) {
            order = Sort.Order.desc(field);
        } else {
            return Sort.unsorted();
        }

        return Sort.by(List.of(order));
    }

    private Pageable createPageable(Integer page, Integer size, Sort sort) {
        if (page == null && size == null) {
            return Pageable.unpaged(sort);
        }

        if (page == null ^ size == null) {
            return Pageable.unpaged(sort);
        }

        return PageRequest.of(page, size, sort);
    }
}