package org.example.controller;



import org.example.entity.Student;
import org.example.service.FirebaseAuthService;
import org.example.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private FirebaseAuthService firebaseAuthService;

    /**
     * Add a new student with course assignment (Admin only)
     * POST /api/students
     * Request body: { "student": {...}, "courseId": 1 }
     */
    @PostMapping
    public ResponseEntity<?> addStudent(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, Object> request) {
        try {
            String firebaseUid = firebaseAuthService.verifyToken(token.replace("Bearer ", ""));

            if (!studentService.isAdmin(firebaseUid)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only admins can add students");
            }

            // Extract student and courseId from request
            Map<String, Object> studentData = (Map<String, Object>) request.get("student");
            Long courseId = request.get("courseId") != null ?
                    Long.valueOf(request.get("courseId").toString()) : null;

            // Create student object
            Student student = new Student();
            student.setStudentName((String) studentData.get("studentName"));
            student.setEmail((String) studentData.get("email"));
            student.setPhone((String) studentData.get("phone"));
            student.setFirebaseUid((String) studentData.get("firebaseUid"));
            student.setRole((String) studentData.getOrDefault("role", "STUDENT"));

            Student createdStudent = studentService.addStudent(student, courseId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Get all students (Admin only)
     * GET /api/students
     */
    @GetMapping
    public ResponseEntity<?> getAllStudents(@RequestHeader("Authorization") String token) {
        try {
            String firebaseUid = firebaseAuthService.verifyToken(token.replace("Bearer ", ""));

            if (!studentService.isAdmin(firebaseUid)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only admins can view all students");
            }

            List<Student> students = studentService.getAllStudents();
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /**
     * Get all students with course details (Admin only)
     * GET /api/students/with-courses
     */
    @GetMapping("/with-courses")
    public ResponseEntity<?> getAllStudentsWithCourses(@RequestHeader("Authorization") String token) {
        try {
            String firebaseUid = firebaseAuthService.verifyToken(token.replace("Bearer ", ""));

            if (!studentService.isAdmin(firebaseUid)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only admins can view all students");
            }

            List<Map<String, Object>> students = studentService.getAllStudentsWithCourseDetails();
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /**
     * Get current user's details (Student can view their own details)
     * GET /api/students/me
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentStudent(@RequestHeader("Authorization") String token) {
        try {
            String firebaseUid = firebaseAuthService.verifyToken(token.replace("Bearer ", ""));
            Student student = studentService.getStudentByFirebaseUid(firebaseUid);
            return ResponseEntity.ok(student);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Get student by ID (Admin only)
     * GET /api/students/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        try {
            String firebaseUid = firebaseAuthService.verifyToken(token.replace("Bearer ", ""));

            if (!studentService.isAdmin(firebaseUid)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only admins can view student details");
            }

            Student student = studentService.getStudentById(id);
            return ResponseEntity.ok(student);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Get students enrolled in a specific course (Admin only)
     * GET /api/students/course/{courseId}
     */
    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getStudentsByCourse(
            @RequestHeader("Authorization") String token,
            @PathVariable Long courseId) {
        try {
            String firebaseUid = firebaseAuthService.verifyToken(token.replace("Bearer ", ""));

            if (!studentService.isAdmin(firebaseUid)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only admins can view students by course");
            }

            List<Student> students = studentService.getStudentsByCourseId(courseId);
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Update student details (Admin only)
     * PUT /api/students/{id}
     * Request body: { "student": {...}, "courseId": 1 }
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {
        try {
            String firebaseUid = firebaseAuthService.verifyToken(token.replace("Bearer ", ""));

            if (!studentService.isAdmin(firebaseUid)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only admins can update students");
            }

            Map<String, Object> studentData = (Map<String, Object>) request.get("student");
            Long courseId = request.get("courseId") != null ?
                    Long.valueOf(request.get("courseId").toString()) : null;

            Student student = new Student();
            student.setStudentName((String) studentData.get("studentName"));
            student.setEmail((String) studentData.get("email"));
            student.setPhone((String) studentData.get("phone"));

            Student updatedStudent = studentService.updateStudent(id, student, courseId);
            return ResponseEntity.ok(updatedStudent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Delete student (Admin only)
     * DELETE /api/students/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        try {
            String firebaseUid = firebaseAuthService.verifyToken(token.replace("Bearer ", ""));

            if (!studentService.isAdmin(firebaseUid)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only admins can delete students");
            }

            studentService.deleteStudent(id);
            return ResponseEntity.ok("Student deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}