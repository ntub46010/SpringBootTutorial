package com.example.demo.controller;

import com.example.demo.model.Certificate;
import com.example.demo.model.Contact;
import com.example.demo.model.Student;
import com.example.demo.param.StudentRequestParam;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class MyController {

    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("/students")
    public ResponseEntity<Void> createStudent(@RequestBody Student student) {
        student.setId(null);
        studentRepository.insert(student);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .build(Map.of("id", student.getId()));

        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable String id) {
        Optional<Student> studentOp = studentRepository.findById(id);
        return studentOp.isPresent()
                ? ResponseEntity.ok(studentOp.get())
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/students/ids")
    public ResponseEntity<List<Student>> getStudents(@RequestParam List<String> idList) {
        List<Student> students = studentRepository.findAllById(idList);
        return ResponseEntity.ok(students);
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<Void> updateStudent(
            @PathVariable String id, @RequestBody Student request
    ) {
        Optional<Student> studentOp = studentRepository.findById(id);
        if (studentOp.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Student student = studentOp.get();
        student.setName(request.getName());
        student.setGrade(request.getGrade());
        student.setBirthday(request.getBirthday());
        student.setContact(request.getContact());
        student.setCertificates(request.getCertificates());
        studentRepository.save(student);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String id) {
        studentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/students/reset")
    public ResponseEntity<Void> resetStudents() {
        Student s1 = new Student();
        s1.setName("Vincent");
        s1.setGrade(2);
        s1.setBirthday(LocalDate.of(1996, 1, 1));
        s1.setContact(Contact.of("vincent@school.com", "0911111111"));
        s1.setCertificates(List.of(
                Certificate.of("GEPT", null, "Medium"),
                Certificate.of("TOEIC", 990, "Gold")
        ));

        Student s2 = new Student();
        s2.setName("Dora");
        s2.setGrade(3);
        s2.setBirthday(LocalDate.of(1995, 1, 1));
        s2.setContact(Contact.of("dora@school.com", "0922222222"));
        s2.setCertificates(List.of(
                Certificate.of("TOEFL", 85, null),
                Certificate.of("TOEIC", 900, "Gold")
        ));

        Student s3 = new Student();
        s3.setName("Ivy");
        s3.setGrade(4);
        s3.setBirthday(LocalDate.of(1994, 1, 1));
        s3.setContact(Contact.of("ivy@school.com", "0933333333"));
        s3.setCertificates(List.of(
                Certificate.of("IELTS", 5, null)
        ));

        studentRepository.deleteAll();
        studentRepository.insert(List.of(s1, s2, s3));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/students")
    public ResponseEntity<List<Student>> getStudents(@ModelAttribute StudentRequestParam param) {
        Sort sort = createSort(param.getSortField(), param.getSortDirection());
        Pageable pageable = createPageable(param.getPage(), param.getSize(), sort);
        List<Student> students = studentRepository.find(pageable);

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