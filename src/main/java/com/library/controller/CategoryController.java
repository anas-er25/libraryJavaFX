package com.library.controller;

import com.library.model.Category;
import com.library.service.CategoryService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class CategoryController {
    @FXML private TextField nameField;
    @FXML private TableView<Category> categoryTable;
    @FXML private TableColumn<Category, Integer> idColumn;
    @FXML private TableColumn<Category, String> nameColumn;

    private final CategoryService categoryService = new CategoryService();
    private ObservableList<Category> categoryList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Set up table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Load categories
        loadCategories();

        // Select row to populate form
        categoryTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nameField.setText(newSelection.getName());
            }
        });
    }

    private void loadCategories() {
        try {
            categoryList.setAll(categoryService.getAllCategories());
            categoryTable.setItems(categoryList);
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to load categories: " + e.getMessage());
        }
    }

    @FXML
    private void addCategory() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            showErrorAlert("Validation Error", "Category name is required.");
            return;
        }

        try {
            if (categoryService.categoryNameExists(name)) {
                showErrorAlert("Validation Error", "A category with this name already exists.");
                return;
            }

            Category category = new Category();
            category.setName(name);
            categoryService.addCategory(category);
            loadCategories();
            clearFields();
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to add category: " + e.getMessage());
        }
    }

    @FXML
    private void updateCategory() {
        Category selected = categoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showErrorAlert("Selection Error", "Please select a category to update.");
            return;
        }

        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            showErrorAlert("Validation Error", "Category name is required.");
            return;
        }

        try {
            // Check if the new name is taken by another category (excluding the current one)
            if (!name.equals(selected.getName()) && categoryService.categoryNameExists(name)) {
                showErrorAlert("Validation Error", "A category with this name already exists.");
                return;
            }

            selected.setName(name);
            categoryService.updateCategory(selected);
            loadCategories();
            clearFields();
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to update category: " + e.getMessage());
        }
    }

    @FXML
    private void deleteCategory() {
        Category selected = categoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showErrorAlert("Selection Error", "Please select a category to delete.");
            return;
        }

        try {
            categoryService.deleteCategory(selected.getId());
            loadCategories();
            clearFields();
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to delete category: " + e.getMessage());
        }
    }

    private void clearFields() {
        nameField.clear();
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}