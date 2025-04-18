package com.library.controller;

import com.library.model.Student;
import com.library.service.StudentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class StudentController {
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField userIdField;
    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, Integer> idColumn;
    @FXML private TableColumn<Student, String> nameColumn;
    @FXML private TableColumn<Student, String> emailColumn;
    @FXML private TableColumn<Student, Integer> userIdColumn;

    private final StudentService studentService = new StudentService();
    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Set up table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

        // Load students
        loadStudents();

        // Select row to populate form
        studentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nameField.setText(newSelection.getName());
                emailField.setText(newSelection.getEmail());
                userIdField.setText(String.valueOf(newSelection.getUserId()));
            }
        });
    }

    private void loadStudents() {
        try {
            studentList.setAll(studentService.getAllStudents());
            studentTable.setItems(studentList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addStudent() {
        try {
            Student student = new Student();
            student.setName(nameField.getText());
            student.setEmail(emailField.getText());
            student.setUserId(Integer.parseInt(userIdField.getText()));
            studentService.addStudent(student);
            loadStudents();
            clearFields();
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateStudent() {
        try {
            Student selected = studentTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setName(nameField.getText());
                selected.setEmail(emailField.getText());
                selected.setUserId(Integer.parseInt(userIdField.getText()));
                studentService.updateStudent(selected);
                loadStudents();
                clearFields();
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteStudent() {
        try {
            Student selected = studentTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                studentService.deleteStudent(selected.getId());
                loadStudents();
                clearFields();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        nameField.clear();
        emailField.clear();
        userIdField.clear();
    }
}