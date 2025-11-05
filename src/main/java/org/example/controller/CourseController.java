package org.example.controller;


import org.example.entity.Course;
import org.example.service.CourseService;
import org.example.service.FirebaseAuthService;
import org.example.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Course Controller - REST API endpoints for Course operations
 */
@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*") // Allow requests from Streamlit UI
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private FirebaseAuthService firebaseAuthService;

    @Autowired
    private StudentService studentService;

    /**
     * Create a new course (Admin only)
     * POST /api/courses
     */
    @PostMapping
    public ResponseEntity<?> createCourse(
            @RequestHeader("Authorization") String token,
            @RequestBody Course course) {
        try {
            // Verify Firebase token
            String firebaseUid = firebaseAuthService.verifyToken(token.replace("Bearer ", ""));

            // Check if user is admin
            if (!studentService.isAdmin(firebaseUid)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only admins can create courses");
            }

            Course createdCourse = courseService.createCourse(course);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Get all courses (Accessible to all authenticated users)
     * GET /api/courses
     */
    @GetMapping
    public ResponseEntity<?> getAllCourses(@RequestHeader("Authorization") String token) {
        try {
            // Verify token
            firebaseAuthService.verifyToken(token.replace("Bearer ", ""));

            List<Course> courses = courseService.getAllCourses();
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /**
     * Get course by ID
     * GET /api/courses/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        try {
            firebaseAuthService.verifyToken(token.replace("Bearer ", ""));

            Course course = courseService.getCourseById(id);
            return ResponseEntity.ok(course);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Update course (Admin only)
     * PUT /api/courses/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestBody Course course) {
        try {
            String firebaseUid = firebaseAuthService.verifyToken(token.replace("Bearer ", ""));

            if (!studentService.isAdmin(firebaseUid)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only admins can update courses");
            }

            Course updatedCourse = courseService.updateCourse(id, course);
            return ResponseEntity.ok(updatedCourse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Delete course (Admin only)
     * DELETE /api/courses/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        try {
            String firebaseUid = firebaseAuthService.verifyToken(token.replace("Bearer ", ""));

            if (!studentService.isAdmin(firebaseUid)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only admins can delete courses");
            }

            courseService.deleteCourse(id);
            return ResponseEntity.ok("Course deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
