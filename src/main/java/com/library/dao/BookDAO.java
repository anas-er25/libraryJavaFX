package com.library.dao;

import com.library.model.Book;
import com.library.model.Category;
import com.library.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    public List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setCategoryId(rs.getInt("category_id"));
                books.add(book);
            }
        }
        return books;
    }

    public void addBook(Book book) throws SQLException {
        String query = "INSERT INTO books (title, author, category_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setInt(3, book.getCategoryId());
            stmt.executeUpdate();
        }
    }

    public void updateBook(Book book) throws SQLException {
        String query = "UPDATE books SET title = ?, author = ?, category_id = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setInt(3, book.getCategoryId());
            stmt.setInt(4, book.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteBook(int id) throws SQLException {
        String query = "DELETE FROM books WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public boolean titleExists(String title) throws SQLException {
        String query = "SELECT COUNT(*) FROM books WHERE title = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, title);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM categories";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                categories.add(category);
            }
        }
        return categories;
    }

    public String getCategoryName(int categoryId) throws SQLException {
        String query = "SELECT name FROM categories WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, categoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        }
        return "";
    }
}