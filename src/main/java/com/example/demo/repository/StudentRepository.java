package com.example.demo.repository;

import com.example.demo.entity.Student;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByName(String name);
    Student findByContactEmail(String email);
    Student findByContactPhone(String phone);

    List<Student> findByGradeGreaterThanEqual(int from);
    List<Student> findByGradeLessThanEqual(int to);
    List<Student> findByBirthdayAfter(LocalDate from);
    List<Student> findByBirthdayBefore(LocalDate to);

    List<Student> findByContactEmailOrContactPhone(String email, String phone);

    @Query(
            nativeQuery = true,
            value = """
                SELECT *
                FROM `student`
                WHERE `contact_email` = ?1 OR `contact_phone` = ?2
            """
    )
    List<Student> findByContact(String email, String phone);

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM `student`"
    )
    List<Student> find(Pageable pageable);
}