package com.library.controller;

import com.library.model.Loan;
import com.library.service.LoanService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.LocalDate;

public class StudentBooksController {
    @FXML private TableView<Loan> booksTable;
    @FXML private TableColumn<Loan, Integer> bookIdColumn;
    @FXML private TableColumn<Loan, String> titleColumn;
    @FXML private TableColumn<Loan, String> authorColumn;
    @FXML private TableColumn<Loan, LocalDate> loanDateColumn;
    @FXML private TableColumn<Loan, LocalDate> returnDateColumn;

    private final LoanService loanService = new LoanService();
    private ObservableList<Loan> loanList = FXCollections.observableArrayList();
    private int studentId;

    // Méthode pour définir l'ID étudiant (appelée par MainController)
    public void setStudentId(int studentId) {
        this.studentId = studentId;
        loadStudentBooks();
    }

    @FXML
    private void initialize() {
        // Configurer les colonnes du tableau
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        titleColumn.setCellValueFactory(cellData -> {
            try {
                return new javafx.beans.property.SimpleStringProperty(loanService.getBookTitle(cellData.getValue().getBookId()));
            } catch (SQLException e) {
                e.printStackTrace();
                return new javafx.beans.property.SimpleStringProperty("");
            }
        });
        authorColumn.setCellValueFactory(cellData -> {
            try {
                return new javafx.beans.property.SimpleStringProperty(loanService.getBookAuthor(cellData.getValue().getBookId()));
            } catch (SQLException e) {
                e.printStackTrace();
                return new javafx.beans.property.SimpleStringProperty("");
            }
        });
        loanDateColumn.setCellValueFactory(new PropertyValueFactory<>("loanDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        // Les données du tableau seront chargées une fois que studentId sera défini
    }

    private void loadStudentBooks() {
        try {
            loanList.setAll(loanService.getLoansByStudentId(studentId));
            booksTable.setItems(loanList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}