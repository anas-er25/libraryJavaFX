package com.library.service;

import com.library.dao.CategoryDAO;
import com.library.model.Category;

import java.sql.SQLException;
import java.util.List;

public class CategoryService {
    private final CategoryDAO categoryDAO = new CategoryDAO();

    public List<Category> getAllCategories() throws SQLException {
        return categoryDAO.getAllCategories();
    }

    public void addCategory(Category category) throws SQLException {
        categoryDAO.addCategory(category);
    }

    public void updateCategory(Category category) throws SQLException {
        categoryDAO.updateCategory(category);
    }

    public void deleteCategory(int id) throws SQLException {
        categoryDAO.deleteCategory(id);
    }

    public boolean categoryNameExists(String name) throws SQLException {
        return categoryDAO.categoryNameExists(name);
    }
}