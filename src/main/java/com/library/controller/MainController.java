package com.library.controller;

import com.library.model.User;
import com.library.util.DatabaseUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainController {
    private User loggedInUser;

    @FXML private Button studentsButton;
    @FXML private Button categoriesButton;
    @FXML private Button booksButton;
    @FXML private Button loansButton;

    // Method to set the logged-in user (called by LoginController)
    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
        updateButtonVisibility();
        if ("STUDENT".equals(user.getRole())) {
            showStudentBooks();
        }
    }

    @FXML
    private void initialize() {
        // Ensure buttons are hidden until user is set
        if (loggedInUser == null) {
            studentsButton.setVisible(false);
            categoriesButton.setVisible(false);
            booksButton.setVisible(false);
            loansButton.setVisible(false);
        }
    }

    private void updateButtonVisibility() {
        boolean isAdmin = "ADMIN".equals(loggedInUser.getRole());
        studentsButton.setVisible(isAdmin);
        categoriesButton.setVisible(isAdmin);
        booksButton.setVisible(isAdmin);
        loansButton.setVisible(isAdmin);
    }

    @FXML
    private void showStudents() throws IOException {
        if (isAdmin()) {
            loadScreen("/fxml/student_management.fxml");
        }
    }

    @FXML
    private void showCategories() throws IOException {
        if (isAdmin()) {
            loadScreen("/fxml/category_management.fxml");
        }
    }

    @FXML
    private void showBooks() throws IOException {
        if (isAdmin()) {
            loadScreen("/fxml/book_management.fxml");
        }
    }

    @FXML
    private void showLoans() throws IOException {
        if (isAdmin()) {
            loadScreen("/fxml/loan_management.fxml");
        }
    }

    private void showStudentBooks() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/student_books.fxml"));
            Parent root = loader.load();
            StudentBooksController controller = loader.getController();
            // Fetch student ID from students table using user ID
            int studentId = getStudentIdByUserId(loggedInUser.getId());
            controller.setStudentId(studentId);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("My Borrowed Books");
            stage.show();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isAdmin() {
        return "ADMIN".equals(loggedInUser.getRole());
    }

    private int getStudentIdByUserId(int userId) throws SQLException {
        String query = "SELECT id FROM students WHERE user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            throw new SQLException("No student found for user ID: " + userId);
        }
    }

    private void loadScreen(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Library Management");
        stage.show();
    }
}