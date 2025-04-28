package com.library.controller;

import com.library.model.Student;
import com.library.model.User;
import com.library.service.StudentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class StudentController {
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private ComboBox<User> userComboBox;
    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, Integer> idColumn;
    @FXML private TableColumn<Student, String> nameColumn;
    @FXML private TableColumn<Student, String> emailColumn;
    @FXML private TableColumn<Student, String> userIdColumn;

    private final StudentService studentService = new StudentService();
    private ObservableList<Student> studentList = FXCollections.observableArrayList();
    private ObservableList<User> userList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Set up table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        userIdColumn.setCellValueFactory(cellData -> {
            try {
                return new javafx.beans.property.SimpleStringProperty(
                        studentService.getUsername(cellData.getValue().getUserId()));
            } catch (SQLException e) {
                return new javafx.beans.property.SimpleStringProperty("");
            }
        });

        // Load students and users
        loadStudents();
        loadUsers();

        // Select row to populate form
        studentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nameField.setText(newSelection.getName());
                emailField.setText(newSelection.getEmail());
                // Select the user in the ComboBox
                userComboBox.getSelectionModel().select(
                        userList.stream()
                                .filter(u -> u.getId() == newSelection.getUserId())
                                .findFirst()
                                .orElse(null)
                );
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

    private void loadUsers() {
        try {
            userList.setAll(studentService.getAllUsers());
            userComboBox.setItems(userList);
            // Set display text for ComboBox
            userComboBox.setCellFactory(cb -> new javafx.scene.control.ListCell<User>() {
                @Override
                protected void updateItem(User item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getUsername());
                }
            });
            userComboBox.setButtonCell(new javafx.scene.control.ListCell<User>() {
                @Override
                protected void updateItem(User item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getUsername());
                }
            });
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to load users: " + e.getMessage());
        }
    }

    @FXML
    private void addStudent() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        User selectedUser = userComboBox.getSelectionModel().getSelectedItem();

        // Validate required fields
        if (name.isEmpty() || email.isEmpty() || selectedUser == null) {
            showErrorAlert("Validation Error", "All fields are required.");
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
            student.setUserId(selectedUser.getId());
            studentService.addStudent(student);
            loadStudents();
            clearFields();
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to add student: " + e.getMessage());
        }
    }

    @FXML
    private void updateStudent() {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            showErrorAlert("Selection Error", "Please select a student to update.");
            return;
        }

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        User selectedUser = userComboBox.getSelectionModel().getSelectedItem();

        // Validate required fields
        if (name.isEmpty() || email.isEmpty() || selectedUser == null) {
            showErrorAlert("Validation Error", "All fields are required.");
            return;
        }

        try {
            // Check email uniqueness (exclude current student)
            if (!email.equals(selectedStudent.getEmail()) && studentService.emailExists(email)) {
                showErrorAlert("Validation Error", "A student with this email already exists.");
                return;
            }

            selectedStudent.setName(name);
            selectedStudent.setEmail(email);
            selectedStudent.setUserId(selectedUser.getId());
            studentService.updateStudent(selectedStudent);
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
        userComboBox.getSelectionModel().clearSelection();
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}