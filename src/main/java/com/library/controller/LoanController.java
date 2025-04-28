package com.library.controller;

import com.library.model.Book;
import com.library.model.Loan;
import com.library.model.Student;
import com.library.service.LoanService;
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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class LoanController {
    @FXML private ComboBox<Student> studentComboBox;
    @FXML private ComboBox<Book> bookComboBox;
    @FXML private TextField loanDateField;
    @FXML private TextField returnDateField;
    @FXML private TableView<Loan> loanTable;
    @FXML private TableColumn<Loan, Integer> idColumn;
    @FXML private TableColumn<Loan, String> studentIdColumn;
    @FXML private TableColumn<Loan, String> bookIdColumn;
    @FXML private TableColumn<Loan, LocalDate> loanDateColumn;
    @FXML private TableColumn<Loan, LocalDate> returnDateColumn;

    private final LoanService loanService = new LoanService();
    private ObservableList<Loan> loanList = FXCollections.observableArrayList();
    private ObservableList<Book> bookList = FXCollections.observableArrayList();
    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Set up table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        studentIdColumn.setCellValueFactory(cellData -> {
            try {
                return new javafx.beans.property.SimpleStringProperty(
                        loanService.getStudentName(cellData.getValue().getStudentId()));
            } catch (SQLException e) {
                return new javafx.beans.property.SimpleStringProperty("");
            }
        });
        bookIdColumn.setCellValueFactory(cellData -> {
            try {
                return new javafx.beans.property.SimpleStringProperty(
                        loanService.getBookTitle(cellData.getValue().getBookId()));
            } catch (SQLException e) {
                return new javafx.beans.property.SimpleStringProperty("");
            }
        });
        loanDateColumn.setCellValueFactory(new PropertyValueFactory<>("loanDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        // Load loans, books, and students
        loadLoans();
        loadBooks();
        loadStudents();

        // Select row to populate form
        loanTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                studentComboBox.getSelectionModel().select(
                        studentList.stream()
                                .filter(s -> s.getId() == newSelection.getStudentId())
                                .findFirst()
                                .orElse(null)
                );
                bookComboBox.getSelectionModel().select(
                        bookList.stream()
                                .filter(b -> b.getId() == newSelection.getBookId())
                                .findFirst()
                                .orElse(null)
                );
                loanDateField.setText(newSelection.getLoanDate().toString());
                returnDateField.setText(newSelection.getReturnDate() != null ? newSelection.getReturnDate().toString() : "");
            }
        });
    }

    private void loadLoans() {
        try {
            loanList.setAll(loanService.getAllLoans());
            loanTable.setItems(loanList);
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to load loans: " + e.getMessage());
        }
    }

    private void loadBooks() {
        try {
            bookList.setAll(loanService.getAllBooks());
            bookComboBox.setItems(bookList);
            // Set display text for ComboBox
            bookComboBox.setCellFactory(cb -> new javafx.scene.control.ListCell<Book>() {
                @Override
                protected void updateItem(Book item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getTitle());
                }
            });
            bookComboBox.setButtonCell(new javafx.scene.control.ListCell<Book>() {
                @Override
                protected void updateItem(Book item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getTitle());
                }
            });
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to load books: " + e.getMessage());
        }
    }

    private void loadStudents() {
        try {
            studentList.setAll(loanService.getAllStudents());
            studentComboBox.setItems(studentList);
            // Set display text for ComboBox
            studentComboBox.setCellFactory(cb -> new javafx.scene.control.ListCell<Student>() {
                @Override
                protected void updateItem(Student item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getName());
                }
            });
            studentComboBox.setButtonCell(new javafx.scene.control.ListCell<Student>() {
                @Override
                protected void updateItem(Student item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getName());
                }
            });
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to load students: " + e.getMessage());
        }
    }

    @FXML
    private void addLoan() {
        Student selectedStudent = studentComboBox.getSelectionModel().getSelectedItem();
        Book selectedBook = bookComboBox.getSelectionModel().getSelectedItem();
        String loanDateText = loanDateField.getText().trim();
        String returnDateText = returnDateField.getText().trim();

        // Validate required fields
        if (selectedStudent == null || selectedBook == null || loanDateText.isEmpty()) {
            showErrorAlert("Validation Error", "Student, Book, and Loan Date are required.");
            return;
        }

        // Validate loanDate format
        LocalDate loanDate;
        try {
            loanDate = LocalDate.parse(loanDateText);
        } catch (DateTimeParseException e) {
            showErrorAlert("Validation Error", "Loan Date must be a valid date (YYYY-MM-DD).");
            return;
        }

        // Validate returnDate format (if provided)
        LocalDate returnDate = null;
        if (!returnDateText.isEmpty()) {
            try {
                returnDate = LocalDate.parse(returnDateText);
                if (returnDate.isBefore(loanDate)) {
                    showErrorAlert("Validation Error", "Return Date cannot be before Loan Date.");
                    return;
                }
            } catch (DateTimeParseException e) {
                showErrorAlert("Validation Error", "Return Date must be a valid date (YYYY-MM-DD).");
                return;
            }
        }

        try {
            // Check for existing active loan
            if (loanService.activeLoanExists(selectedStudent.getId(), selectedBook.getId())) {
                showErrorAlert("Validation Error", "An active loan for this student and book already exists.");
                return;
            }

            Loan loan = new Loan();
            loan.setStudentId(selectedStudent.getId());
            loan.setBookId(selectedBook.getId());
            loan.setLoanDate(loanDate);
            loan.setReturnDate(returnDate);
            loanService.addLoan(loan);
            loadLoans();
            clearFields();
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to add loan: " + e.getMessage());
        }
    }

    @FXML
    private void updateLoan() {
        Loan selectedLoan = loanTable.getSelectionModel().getSelectedItem();
        if (selectedLoan == null) {
            showErrorAlert("Selection Error", "Please select a loan to update.");
            return;
        }

        Student selectedStudent = studentComboBox.getSelectionModel().getSelectedItem();
        Book selectedBook = bookComboBox.getSelectionModel().getSelectedItem();
        String loanDateText = loanDateField.getText().trim();
        String returnDateText = returnDateField.getText().trim();

        // Validate required fields
        if (selectedStudent == null || selectedBook == null || loanDateText.isEmpty()) {
            showErrorAlert("Validation Error", "Student, Book, and Loan Date are required.");
            return;
        }

        // Validate loanDate format
        LocalDate loanDate;
        try {
            loanDate = LocalDate.parse(loanDateText);
        } catch (DateTimeParseException e) {
            showErrorAlert("Validation Error", "Loan Date must be a valid date (YYYY-MM-DD).");
            return;
        }

        // Validate returnDate format (if provided)
        LocalDate returnDate = null;
        if (!returnDateText.isEmpty()) {
            try {
                returnDate = LocalDate.parse(returnDateText);
                if (returnDate.isBefore(loanDate)) {
                    showErrorAlert("Validation Error", "Return Date cannot be before Loan Date.");
                    return;
                }
            } catch (DateTimeParseException e) {
                showErrorAlert("Validation Error", "Return Date must be a valid date (YYYY-MM-DD).");
                return;
            }
        }

        try {
            // Check for existing active loan (exclude current loan)
            if ((selectedStudent.getId() != selectedLoan.getStudentId() || selectedBook.getId() != selectedLoan.getBookId()) &&
                    loanService.activeLoanExists(selectedStudent.getId(), selectedBook.getId())) {
                showErrorAlert("Validation Error", "An active loan for this student and book already exists.");
                return;
            }

            selectedLoan.setStudentId(selectedStudent.getId());
            selectedLoan.setBookId(selectedBook.getId());
            selectedLoan.setLoanDate(loanDate);
            selectedLoan.setReturnDate(returnDate);
            loanService.updateLoan(selectedLoan);
            loadLoans();
            clearFields();
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to update loan: " + e.getMessage());
        }
    }

    @FXML
    private void deleteLoan() {
        Loan selected = loanTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showErrorAlert("Selection Error", "Please select a loan to delete.");
            return;
        }

        try {
            loanService.deleteLoan(selected.getId());
            loadLoans();
            clearFields();
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to delete loan: " + e.getMessage());
        }
    }

    private void clearFields() {
        studentComboBox.getSelectionModel().clearSelection();
        bookComboBox.getSelectionModel().clearSelection();
        loanDateField.clear();
        returnDateField.clear();
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}