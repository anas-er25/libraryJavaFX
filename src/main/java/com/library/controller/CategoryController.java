package com.library.controller;

import com.library.model.Category;
import com.library.service.CategoryService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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
            e.printStackTrace();
        }
    }

    @FXML
    private void addCategory() {
        try {
            Category category = new Category();
            category.setName(nameField.getText());
            categoryService.addCategory(category);
            loadCategories();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateCategory() {
        try {
            Category selected = categoryTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setName(nameField.getText());
                categoryService.updateCategory(selected);
                loadCategories();
                clearFields();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteCategory() {
        try {
            Category selected = categoryTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                categoryService.deleteCategory(selected.getId());
                loadCategories();
                clearFields();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        nameField.clear();
    }
}