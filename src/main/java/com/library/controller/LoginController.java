package com.library.controller;

import com.library.model.User;
import com.library.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    private UserService userService = new UserService();

    @FXML
    private void handleLogin() {
        try {
            User user = userService.login(usernameField.getText(), passwordField.getText());
            if (user != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
                Parent root = loader.load();
                MainController mainController = loader.getController();
                mainController.setLoggedInUser(user); // Pass the logged-in user
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Library Management Dashboard");
                stage.show();
            } else {
                System.out.println("Invalid credentials");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Register New User");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}