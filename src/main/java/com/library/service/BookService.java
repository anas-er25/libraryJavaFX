package com.library.service;

import com.library.dao.BookDAO;
import com.library.model.Book;

import java.sql.SQLException;
import java.util.List;

public class BookService {
    private final BookDAO bookDAO = new BookDAO();

    public void addBook(Book book) throws SQLException {
        bookDAO.addBook(book);
    }

    public void updateBook(Book book) throws SQLException {
        bookDAO.updateBook(book);
    }

    public void deleteBook(int id) throws SQLException {
        bookDAO.deleteBook(id);
    }

    public List<Book> getAllBooks() throws SQLException {
        return bookDAO.getAllBooks();
    }
}