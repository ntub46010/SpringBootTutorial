package com.example.demo.repository;

import com.example.demo.model.Student;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
    Student findByName(String s);
    Student findByContactEmail(String s);
    Student findByContactPhone(String s);
    List<Student> findByCertificatesType(String certType);

    List<Student> findByGradeGreaterThanEqual(int from);
    List<Student> findByGradeLessThanEqual(int to);
    List<Student> findByBirthdayAfter(LocalDate from);
    List<Student> findByBirthdayBefore(LocalDate to);
    List<Student> findByGradeBetween(Range<Integer> range);

    List<Student> findByContactEmailOrContactPhone(String email, String phone);

    @Query("""
        {
            "$or": [
                { "contact.email": ?0 },
                { "contact.phone": ?1 }
            ]
        }
    """)
    List<Student> findByContact(String email, String phone);

    @Query("""
        {
            "certificates": {
                "$elemMatch": {
                    "type": ?0,
                    "score": { "$gte": ?1 }
                }
            }
        }
    """)
    List<Student> findByCertificateTypeAndScoreGte(String type, int score);

    List<Student> findAllByOrderByGradeDesc();

    @Query("{}")
    List<Student> find(Sort sort);

    @Query("{}")
    List<Student> find(Pageable pageable);
}