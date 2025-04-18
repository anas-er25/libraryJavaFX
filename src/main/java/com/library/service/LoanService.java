package com.library.service;

import com.library.dao.LoanDAO;
import com.library.model.Loan;

import java.sql.SQLException;
import java.util.List;

public class LoanService {
    private final LoanDAO loanDAO = new LoanDAO();

    public List<Loan> getAllLoans() throws SQLException {
        return loanDAO.getAllLoans();
    }

    public void addLoan(Loan loan) throws SQLException {
        loanDAO.addLoan(loan);
    }

    public void updateLoan(Loan loan) throws SQLException {
        loanDAO.updateLoan(loan);
    }

    public void deleteLoan(int id) throws SQLException {
        loanDAO.deleteLoan(id);
    }

    public List<Loan> getLoansByStudentId(int studentId) throws SQLException {
        return loanDAO.getLoansByStudentId(studentId);
    }

    public String getBookTitle(int bookId) throws SQLException {
        return loanDAO.getBookTitle(bookId);
    }

    public String getBookAuthor(int bookId) throws SQLException {
        return loanDAO.getBookAuthor(bookId);
    }
}