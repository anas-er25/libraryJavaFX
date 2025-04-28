package com.library.controller;

import com.library.model.Student;
import com.library.service.StudentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
            showErrorAlert("Database Error", "Failed to load students: " + e.getMessage());
        }
    }

    @FXML
    private void addStudent() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String userIdText = userIdField.getText().trim();

        // Validate required fields
        if (name.isEmpty() || email.isEmpty() || userIdText.isEmpty()) {
            showErrorAlert("Validation Error", "All fields are required.");
            return;
        }

        // Validate userId format
        int userId;
        try {
            userId = Integer.parseInt(userIdText);
        } catch (NumberFormatException e) {
            showErrorAlert("Validation Error", "User ID must be a valid number.");
            return;
        }

        try {
            // Check email uniqueness
            if (studentService.emailExists(email)) {
                showErrorAlert("Validation Error", "A student with this email already exists.");
                return;
            }

            Student student = new Student();
            student.setName(name);
            student.setEmail(email);
            student.setUserId(userId);
            studentService.addStudent(student);
            loadStudents();
            clearFields();
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to add student: " + e.getMessage());
        }
    }

    @FXML
    private void updateStudent() {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showErrorAlert("Selection Error", "Please select a student to update.");
            return;
        }

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String userIdText = userIdField.getText().trim();

        // Validate required fields
        if (name.isEmpty() || email.isEmpty() || userIdText.isEmpty()) {
            showErrorAlert("Validation Error", "All fields are required.");
            return;
        }

        // Validate userId format
        int userId;
        try {
            userId = Integer.parseInt(userIdText);
        } catch (NumberFormatException e) {
            showErrorAlert("Validation Error", "User ID must be a valid number.");
            return;
        }

        try {
            // Check email uniqueness (exclude current student)
            if (!email.equals(selected.getEmail()) && studentService.emailExists(email)) {
                showErrorAlert("Validation Error", "A student with this email already exists.");
                return;
            }

            selected.setName(name);
            selected.setEmail(email);
            selected.setUserId(userId);
            studentService.updateStudent(selected);
            loadStudents();
            clearFields();
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to update student: " + e.getMessage());
        }
    }

    @FXML
    private void deleteStudent() {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showErrorAlert("Selection Error", "Please select a student to delete.");
            return;
        }

        try {
            studentService.deleteStudent(selected.getId());
            loadStudents();
            clearFields();
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to delete student: " + e.getMessage());
        }
    }

    private void clearFields() {
        nameField.clear();
        emailField.clear();
        userIdField.clear();
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}