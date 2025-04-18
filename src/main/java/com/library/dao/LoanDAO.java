package com.library.dao;

import com.library.model.Loan;
import com.library.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {
    public List<Loan> getAllLoans() throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String query = "SELECT * FROM loans";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Loan loan = new Loan();
                loan.setId(rs.getInt("id"));
                loan.setStudentId(rs.getInt("student_id"));
                loan.setBookId(rs.getInt("book_id"));
                loan.setLoanDate(rs.getDate("loan_date").toLocalDate());
                loan.setReturnDate(rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null);
                loans.add(loan);
            }
        }
        return loans;
    }

    public void addLoan(Loan loan) throws SQLException {
        String query = "INSERT INTO loans (student_id, book_id, loan_date, return_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, loan.getStudentId());
            stmt.setInt(2, loan.getBookId());
            stmt.setDate(3, java.sql.Date.valueOf(loan.getLoanDate()));
            stmt.setDate(4, loan.getReturnDate() != null ? java.sql.Date.valueOf(loan.getReturnDate()) : null);
            stmt.executeUpdate();
        }
    }

    public void updateLoan(Loan loan) throws SQLException {
        String query = "UPDATE loans SET student_id = ?, book_id = ?, loan_date = ?, return_date = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, loan.getStudentId());
            stmt.setInt(2, loan.getBookId());
            stmt.setDate(3, java.sql.Date.valueOf(loan.getLoanDate()));
            stmt.setDate(4, loan.getReturnDate() != null ? java.sql.Date.valueOf(loan.getReturnDate()) : null);
            stmt.setInt(5, loan.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteLoan(int id) throws SQLException {
        String query = "DELETE FROM loans WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Loan> getLoansByStudentId(int studentId) throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String query = "SELECT * FROM loans WHERE student_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Loan loan = new Loan();
                    loan.setId(rs.getInt("id"));
                    loan.setStudentId(rs.getInt("student_id"));
                    loan.setBookId(rs.getInt("book_id"));
                    loan.setLoanDate(rs.getDate("loan_date").toLocalDate());
                    loan.setReturnDate(rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null);
                    loans.add(loan);
                }
            }
        }
        return loans;
    }

    public String getBookTitle(int bookId) throws SQLException {
        String query = "SELECT title FROM books WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("title");
                }
            }
        }
        return "";
    }

    public String getBookAuthor(int bookId) throws SQLException {
        String query = "SELECT author FROM books WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("author");
                }
            }
        }
        return "";
    }
}