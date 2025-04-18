package com.library.model;

import java.time.LocalDate;

public class Loan {
    private int id;
    private int studentId;
    private int bookId;
    private LocalDate loanDate;
    private LocalDate returnDate;

    // Constructor
    public Loan() {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    // toString (optional, for debugging)
    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", studentId=" + studentId +
                ", bookId=" + bookId +
                ", loanDate=" + loanDate +
                ", returnDate=" + returnDate +
                '}';
    }
}