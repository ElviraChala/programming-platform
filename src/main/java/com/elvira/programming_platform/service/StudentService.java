package com.elvira.programming_platform.service;

import com.elvira.programming_platform.coverter.StudentConverter;
import com.elvira.programming_platform.dto.StudentDTO;
import com.elvira.programming_platform.model.CheckKnowledge;
import com.elvira.programming_platform.model.Course;
import com.elvira.programming_platform.model.Lesson;
import com.elvira.programming_platform.model.ProgrammingTask;
import com.elvira.programming_platform.model.Student;
import com.elvira.programming_platform.model.enums.Level;
import com.elvira.programming_platform.repository.ProgrammingTaskRepository;
import com.elvira.programming_platform.repository.StudentRepository;
import com.elvira.programming_platform.repository.check.CheckKnowledgeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentConverter studentConverter;
    private final PasswordEncoder passwordEncoder;
    private final CheckKnowledgeRepository checkKnowledgeRepository;
    private final ProgrammingTaskRepository programmingTaskRepository;

    public StudentService(StudentRepository studentRepository,
                          StudentConverter studentConverter,
                          PasswordEncoder passwordEncoder,
                          CheckKnowledgeRepository checkKnowledgeRepository,
                          ProgrammingTaskRepository programmingTaskRepository) {
        this.studentRepository = studentRepository;
        this.studentConverter = studentConverter;
        this.passwordEncoder = passwordEncoder;
        this.checkKnowledgeRepository = checkKnowledgeRepository;
        this.programmingTaskRepository = programmingTaskRepository;
    }

    public StudentDTO createStudent(StudentDTO studentDTO) {
        Student studentModel = studentConverter.toModel(studentDTO);
        studentModel.setPassword(passwordEncoder.encode(studentModel.getPassword()));
        Student savedStudent = studentRepository.save(studentModel);
        return studentConverter.toDTO(savedStudent);
    }

    public StudentDTO readStudentById(Long userId) {
        Student findStudent = studentRepository.findById(userId).orElseThrow();
        return studentConverter.toDTO(findStudent);
    }

    public List<StudentDTO> readAllStudents() {
        List<Student> findStudents = (List<Student>) studentRepository.findAll();
        return findStudents.stream()
                .map(studentConverter::toDTO)
                .toList();
    }

    public StudentDTO updateStudent(StudentDTO newStudentDTO) {
        Long userId = newStudentDTO.getId();
        Student existingStudent = studentRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + userId));

        existingStudent.setName(newStudentDTO.getName());
        existingStudent.setEmail(newStudentDTO.getEmail());
        existingStudent.setIsFirst(newStudentDTO.getIsFirst());

        if (newStudentDTO.getPassword() != null && !newStudentDTO.getPassword().isBlank()) {
            String encodedPassword = passwordEncoder.encode(newStudentDTO.getPassword().trim());
            existingStudent.setPassword(encodedPassword);
        }

        Student updatedStudent = studentRepository.save(existingStudent);
        return studentConverter.toDTO(updatedStudent);
    }

    public void deleteStudent(String username) {
        if (studentRepository.existsByUsername(username)) {
            studentRepository.deleteByUsername(username);
        }
    }

    public void deleteStudent(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
        }
    }

    public StudentDTO readStudentByName(String username) {
        Student student = studentRepository.findByUsername(username).orElseThrow();
        return studentConverter.toDTO(student);
    }

    public boolean isStudentExists(String email) {
        return studentRepository.findByEmail(email).isPresent();
    }

    public void addScore(Long id, Long checkKnowledgeId) {
        Student student = studentRepository.findById(id).orElseThrow();
        CheckKnowledge checkKnowledge = checkKnowledgeRepository.findById(checkKnowledgeId)
                .orElseThrow(() -> new EntityNotFoundException("CheckKnowledge not found with id: " + checkKnowledgeId));

        student.addPassedTest(checkKnowledge);
        checkAndUpdateLevel(student);
        studentRepository.save(student);
    }

    public List<StudentDTO> getAllSortedByScore() {
        List<Student> students = studentRepository.findAllStudentsOrderedByScore();
        return students.stream()
                .map(studentConverter::toDTO)
                .toList();
    }

    public void addCompletedTask(Long studentId, Long taskId) {
        Student student = studentRepository.findById(studentId).orElseThrow();
        ProgrammingTask task = programmingTaskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("ProgrammingTask not found with id: " + taskId));

        student.addCompletedTask(task);
        checkAndUpdateLevel(student);
        studentRepository.save(student);
    }

    private void checkAndUpdateLevel(Student student) {
        Level currentLevel = student.getLevel();

        // Get all courses for the student's current level
        Set<Course> levelCourses = student.getCourses().stream()
                .filter(course -> course.getLevel() == currentLevel)
                .collect(Collectors.toSet());

        if (levelCourses.isEmpty()) {
            return; // No courses for current level
        }

        // Get all lessons from these courses
        Set<Lesson> levelLessons = levelCourses.stream()
                .flatMap(course -> course.getLessons().stream())
                .collect(Collectors.toSet());

        // Get all CheckKnowledge and ProgrammingTask for this level
        Set<CheckKnowledge> levelCheckKnowledge = levelLessons.stream()
                .map(Lesson::getCheckKnowledge)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<ProgrammingTask> levelProgrammingTasks = levelLessons.stream()
                .flatMap(lesson -> lesson.getProgrammingTasks().stream())
                .collect(Collectors.toSet());

        // Check if student has completed all tasks for current level
        boolean allCheckKnowledgeCompleted = student.getPassedTests().containsAll(levelCheckKnowledge);
        boolean allProgrammingTasksCompleted = student.getCompletedTasks().containsAll(levelProgrammingTasks);

        // If all tasks are completed, increase the level
        if (allCheckKnowledgeCompleted
                && allProgrammingTasksCompleted
                && !levelCheckKnowledge.isEmpty()
                && !levelProgrammingTasks.isEmpty()) {
            if (currentLevel == Level.LOW) {
                student.setLevel(Level.MEDIUM);
            } else if (currentLevel == Level.MEDIUM) {
                student.setLevel(Level.HIGH);
            }
        }
    }
}
