package com.library.controller;

import com.library.model.Loan;
import com.library.service.LoanService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class LoanController {
    @FXML private TextField studentIdField;
    @FXML private TextField bookIdField;
    @FXML private TextField loanDateField;
    @FXML private TextField returnDateField;
    @FXML private TableView<Loan> loanTable;
    @FXML private TableColumn<Loan, Integer> idColumn;
    @FXML private TableColumn<Loan, Integer> studentIdColumn;
    @FXML private TableColumn<Loan, Integer> bookIdColumn;
    @FXML private TableColumn<Loan, LocalDate> loanDateColumn;
    @FXML private TableColumn<Loan, LocalDate> returnDateColumn;

    private final LoanService loanService = new LoanService();
    private ObservableList<Loan> loanList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Set up table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        loanDateColumn.setCellValueFactory(new PropertyValueFactory<>("loanDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        // Load loans
        loadLoans();

        // Select row to populate form
        loanTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                studentIdField.setText(String.valueOf(newSelection.getStudentId()));
                bookIdField.setText(String.valueOf(newSelection.getBookId()));
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
            e.printStackTrace();
        }
    }

    @FXML
    private void addLoan() {
        try {
            Loan loan = new Loan();
            loan.setStudentId(Integer.parseInt(studentIdField.getText()));
            loan.setBookId(Integer.parseInt(bookIdField.getText()));
            loan.setLoanDate(LocalDate.parse(loanDateField.getText()));
            loan.setReturnDate(returnDateField.getText().isEmpty() ? null : LocalDate.parse(returnDateField.getText()));
            loanService.addLoan(loan);
            loadLoans();
            clearFields();
        } catch (SQLException | NumberFormatException | DateTimeParseException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateLoan() {
        try {
            Loan selected = loanTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setStudentId(Integer.parseInt(studentIdField.getText()));
                selected.setBookId(Integer.parseInt(bookIdField.getText()));
                selected.setLoanDate(LocalDate.parse(loanDateField.getText()));
                selected.setReturnDate(returnDateField.getText().isEmpty() ? null : LocalDate.parse(returnDateField.getText()));
                loanService.updateLoan(selected);
                loadLoans();
                clearFields();
            }
        } catch (SQLException | NumberFormatException | DateTimeParseException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteLoan() {
        try {
            Loan selected = loanTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                loanService.deleteLoan(selected.getId());
                loadLoans();
                clearFields();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        studentIdField.clear();
        bookIdField.clear();
        loanDateField.clear();
        returnDateField.clear();
    }
}