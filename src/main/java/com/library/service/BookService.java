package com.library.service;

import com.library.dao.BookDAO;
import com.library.model.Book;
import com.library.model.Category;

import java.sql.SQLException;
import java.util.List;

public class BookService {
    private final BookDAO bookDAO = new BookDAO();

    public List<Book> getAllBooks() throws SQLException {
        return bookDAO.getAllBooks();
    }

    public void addBook(Book book) throws SQLException {
        bookDAO.addBook(book);
    }

    public void updateBook(Book book) throws SQLException {
        bookDAO.updateBook(book);
    }

    public void deleteBook(int id) throws SQLException {
        bookDAO.deleteBook(id);
    }

    public boolean titleExists(String title) throws SQLException {
        return bookDAO.titleExists(title);
    }

    public List<Category> getAllCategories() throws SQLException {
        return bookDAO.getAllCategories();
    }

    public String getCategoryName(int categoryId) throws SQLException {
        return bookDAO.getCategoryName(categoryId);
    }
}