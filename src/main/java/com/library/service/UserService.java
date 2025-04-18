package com.library.service;

import com.library.dao.UserDAO;
import com.library.model.User;

import java.sql.SQLException;

public class UserService {
    private final UserDAO userDAO = new UserDAO();

    public User login(String username, String password) throws SQLException {
        return userDAO.getUserByUsernameAndPassword(username, password);
    }

    public void addUser(User user) throws SQLException {
        userDAO.addUser(user);
    }
}