package com.library.service;

import com.library.dao.StudentDAO;
import com.library.model.Student;

import java.sql.SQLException;
import java.util.List;

public class StudentService {
    private final StudentDAO studentDAO = new StudentDAO();

    public List<Student> getAllStudents() throws SQLException {
        return studentDAO.getAllStudents();
    }

    public void addStudent(Student student) throws SQLException {
        studentDAO.addStudent(student);
    }

    public void updateStudent(Student student) throws SQLException {
        studentDAO.updateStudent(student);
    }

    public void deleteStudent(int id) throws SQLException {
        studentDAO.deleteStudent(id);
    }

    public boolean emailExists(String email) throws SQLException {
        return studentDAO.emailExists(email);
    }
}