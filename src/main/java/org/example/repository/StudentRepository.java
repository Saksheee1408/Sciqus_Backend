package org.example.repository;

import org.example.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {


    Optional<Student> findByEmail(String email);


    Optional<Student> findByFirebaseUid(String firebaseUid);


    @Query("SELECT s FROM Student s WHERE s.course.courseId = :courseId")
    List<Student> findStudentsByCourseId(@Param("courseId") Long courseId);


    boolean existsByEmail(String email);
}