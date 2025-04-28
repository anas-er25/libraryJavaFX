package com.library.controller;

import com.library.model.Book;
import com.library.model.Category;
import com.library.service.BookService;
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

public class BookController {
    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private ComboBox<Category> categoryComboBox;
    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book, Integer> idColumn;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, String> categoryIdColumn;

    private final BookService bookService = new BookService();
    private ObservableList<Book> bookList = FXCollections.observableArrayList();
    private ObservableList<Category> categoryList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Set up table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        categoryIdColumn.setCellValueFactory(cellData -> {
            try {
                return new javafx.beans.property.SimpleStringProperty(
                        bookService.getCategoryName(cellData.getValue().getCategoryId()));
            } catch (SQLException e) {
                return new javafx.beans.property.SimpleStringProperty("");
            }
        });

        // Load books and categories
        loadBooks();
        loadCategories();

        // Select row to populate form
        bookTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                titleField.setText(newSelection.getTitle());
                authorField.setText(newSelection.getAuthor());
                // Select the category in the ComboBox
                categoryComboBox.getSelectionModel().select(
                        categoryList.stream()
                                .filter(c -> c.getId() == newSelection.getCategoryId())
                                .findFirst()
                                .orElse(null)
                );
            }
        });
    }

    private void loadBooks() {
        try {
            bookList.setAll(bookService.getAllBooks());
            bookTable.setItems(bookList);
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to load books: " + e.getMessage());
        }
    }

    private void loadCategories() {
        try {
            categoryList.setAll(bookService.getAllCategories());
            categoryComboBox.setItems(categoryList);
            // Set display text for ComboBox
            categoryComboBox.setCellFactory(cb -> new javafx.scene.control.ListCell<Category>() {
                @Override
                protected void updateItem(Category item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getName());
                }
            });
            categoryComboBox.setButtonCell(new javafx.scene.control.ListCell<Category>() {
                @Override
                protected void updateItem(Category item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getName());
                }
            });
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to load categories: " + e.getMessage());
        }
    }

    @FXML
    private void addBook() {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        Category selectedCategory = categoryComboBox.getSelectionModel().getSelectedItem();

        // Validate required fields
        if (title.isEmpty() || author.isEmpty() || selectedCategory == null) {
            showErrorAlert("Validation Error", "All fields are required.");
            return;
        }

        try {
            // Check title uniqueness
            if (bookService.titleExists(title)) {
                showErrorAlert("Validation Error", "A book with this title already exists.");
                return;
            }

            Book book = new Book();
            book.setTitle(title);
            book.setAuthor(author);
            book.setCategoryId(selectedCategory.getId());
            bookService.addBook(book);
            loadBooks();
            clearFields();
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to add book: " + e.getMessage());
        }
    }

    @FXML
    private void updateBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showErrorAlert("Selection Error", "Please select a book to update.");
            return;
        }

        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        Category selectedCategory = categoryComboBox.getSelectionModel().getSelectedItem();

        // Validate required fields
        if (title.isEmpty() || author.isEmpty() || selectedCategory == null) {
            showErrorAlert("Validation Error", "All fields are required.");
            return;
        }

        try {
            // Check title uniqueness (exclude current book)
            if (!title.equals(selectedBook.getTitle()) && bookService.titleExists(title)) {
                showErrorAlert("Validation Error", "A book with this title already exists.");
                return;
            }

            selectedBook.setTitle(title);
            selectedBook.setAuthor(author);
            selectedBook.setCategoryId(selectedCategory.getId());
            bookService.updateBook(selectedBook);
            loadBooks();
            clearFields();
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to update book: " + e.getMessage());
        }
    }

    @FXML
    private void deleteBook() {
        Book selected = bookTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showErrorAlert("Selection Error", "Please select a book to delete.");
            return;
        }

        try {
            bookService.deleteBook(selected.getId());
            loadBooks();
            clearFields();
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to delete book: " + e.getMessage());
        }
    }

    private void clearFields() {
        titleField.clear();
        authorField.clear();
        categoryComboBox.getSelectionModel().clearSelection();
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}