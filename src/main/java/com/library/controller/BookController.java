package com.library.controller;

import com.library.model.Book;
import com.library.service.BookService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class BookController {
    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField categoryIdField;
    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book, Integer> idColumn;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, Integer> categoryIdColumn;

    private final BookService bookService = new BookService();
    private ObservableList<Book> bookList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Set up table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        categoryIdColumn.setCellValueFactory(new PropertyValueFactory<>("categoryId"));

        // Load books
        loadBooks();

        // Select row to populate form
        bookTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                titleField.setText(newSelection.getTitle());
                authorField.setText(newSelection.getAuthor());
                categoryIdField.setText(String.valueOf(newSelection.getCategoryId()));
            }
        });
    }

    private void loadBooks() {
        try {
            bookList.setAll(bookService.getAllBooks());
            bookTable.setItems(bookList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addBook() {
        try {
            Book book = new Book();
            book.setTitle(titleField.getText());
            book.setAuthor(authorField.getText());
            book.setCategoryId(Integer.parseInt(categoryIdField.getText()));
            bookService.addBook(book);
            loadBooks();
            clearFields();
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateBook() {
        try {
            Book selected = bookTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setTitle(titleField.getText());
                selected.setAuthor(authorField.getText());
                selected.setCategoryId(Integer.parseInt(categoryIdField.getText()));
                bookService.updateBook(selected);
                loadBooks();
                clearFields();
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteBook() {
        try {
            Book selected = bookTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                bookService.deleteBook(selected.getId());
                loadBooks();
                clearFields();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        titleField.clear();
        authorField.clear();
        categoryIdField.clear();
    }
}