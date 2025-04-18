package com.library.controller;

import com.library.model.User;
import com.library.service.UserService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ChoiceBox<String> roleChoiceBox;

    private final UserService userService = new UserService();

    @FXML
    private void initialize() {
        // Populate role choice box
        roleChoiceBox.setItems(FXCollections.observableArrayList("ADMIN", "STUDENT"));
        roleChoiceBox.setValue("STUDENT"); // Default role
    }

    @FXML
    private void handleRegister() {
        try {
            // Validate input
            String username = usernameField.getText();
            String password = passwordField.getText();
            String role = roleChoiceBox.getValue();

            if (username.isEmpty() || password.isEmpty() || role == null) {
                System.out.println("All fields are required");
                return;
            }

            // Create and save user
            User user = new User();
            user.setUsername(username);
            user.setPassword(password); // Consider hashing in production
            user.setRole(role);
            userService.addUser(user);

            // Navigate back to login
            backToLogin();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Registration failed: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void backToLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Library Management System");
        stage.show();
    }
}