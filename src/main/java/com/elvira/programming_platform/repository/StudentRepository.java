package com.elvira.programming_platform.repository;

import com.elvira.programming_platform.model.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends CrudRepository<Student, Long> {
    Optional<Student> findByUsername(String username);

    boolean existsByUsername(String username);

    void deleteByUsername(String username);

    Optional<Student> findByEmail(String email);

    @Query("SELECT s FROM Student s WHERE s.role = 'STUDENT' ORDER BY s.score DESC")
    List<Student> findAllStudentsOrderedByScore();

}
