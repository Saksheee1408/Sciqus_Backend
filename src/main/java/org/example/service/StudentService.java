package org.example.service;



import org.example.entity.Course;
import org.example.entity.Student;
import org.example.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Student Service - Business logic for Student operations
 */
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseService courseService;

    /**
     * Add a new student with course assignment
     */
    @Transactional
    public Student addStudent(Student student, Long courseId) {
        // Check if email already exists
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new RuntimeException("Student with email already exists: " + student.getEmail());
        }

        // Validate and assign course if courseId is provided
        if (courseId != null) {
            Course course = courseService.getCourseById(courseId);
            student.setCourse(course);
        }

        return studentRepository.save(student);
    }

    /**
     * Get all students
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * Get all students with course details
     */
    public List<Map<String, Object>> getAllStudentsWithCourseDetails() {
        List<Student> students = studentRepository.findAll();
        return students.stream().map(student -> {
            Map<String, Object> studentMap = new HashMap<>();
            studentMap.put("studentId", student.getStudentId());
            studentMap.put("studentName", student.getStudentName());
            studentMap.put("email", student.getEmail());
            studentMap.put("phone", student.getPhone());
            studentMap.put("role", student.getRole());

            if (student.getCourse() != null) {
                Map<String, Object> courseMap = new HashMap<>();
                courseMap.put("courseId", student.getCourse().getCourseId());
                courseMap.put("courseName", student.getCourse().getCourseName());
                courseMap.put("courseCode", student.getCourse().getCourseCode());
                courseMap.put("courseDuration", student.getCourse().getCourseDuration());
                studentMap.put("course", courseMap);
            } else {
                studentMap.put("course", null);
            }

            return studentMap;
        }).toList();
    }

    /**
     * Get student by ID
     */
    public Student getStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));
    }

    /**
     * Get student by Firebase UID
     */
    public Student getStudentByFirebaseUid(String firebaseUid) {
        return studentRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new RuntimeException("Student not found with Firebase UID: " + firebaseUid));
    }

    /**
     * Get students enrolled in a specific course
     */
    public List<Student> getStudentsByCourseId(Long courseId) {
        return studentRepository.findStudentsByCourseId(courseId);
    }

    /**
     * Update student details including course modification
     */
    @Transactional
    public Student updateStudent(Long studentId, Student updatedStudent, Long newCourseId) {
        Student existingStudent = getStudentById(studentId);

        // Update basic fields
        existingStudent.setStudentName(updatedStudent.getStudentName());
        existingStudent.setEmail(updatedStudent.getEmail());
        existingStudent.setPhone(updatedStudent.getPhone());

        // Update course if newCourseId is provided
        if (newCourseId != null) {
            Course newCourse = courseService.getCourseById(newCourseId);
            existingStudent.setCourse(newCourse);
        } else {
            existingStudent.setCourse(null); // Remove course association
        }

        return studentRepository.save(existingStudent);
    }

    /**
     * Delete student by ID
     */
    @Transactional
    public void deleteStudent(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new RuntimeException("Student not found with ID: " + studentId);
        }
        studentRepository.deleteById(studentId);
    }

    /**
     * Check if user is admin
     */
    public boolean isAdmin(String firebaseUid) {
        Student student = studentRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return "ADMIN".equals(student.getRole());
    }
}
