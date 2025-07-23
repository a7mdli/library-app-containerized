package storage;

import entities.Admin;
import entities.Book;
import entities.RegularUser;
import entities.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.List;

public class StorageManager {
    private static String DB_URL;
    private static String USER;
    private static String PASSWORD;
    private static Connection connection;

    private static final StorageManager storageManager = new StorageManager();
    public static StorageManager getStorageManager() {
        return storageManager;
    }

    private StorageManager() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("src/storage/config.properties")) {
            props.load(fis);
            DB_URL = props.getProperty("db.url");
            USER = props.getProperty("db.user");
            PASSWORD = props.getProperty("db.password");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addUser(String id, String name, String role) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "InsertINSERT INTO USER (id, name, role) VALUES\n" +
                            "  ('?', '?', '?'),"
            );
            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.setString(3, role);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public void addBook(String id, String title, String author, String genre, int availableCopies) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO BOOK (id, title, author, genre, available_copies) VALUES (?, ?, ?, ?, ?)"
            );
            stmt.setString(1, id);
            stmt.setString(2, title);
            stmt.setString(3, author);
            stmt.setString(4, genre);
            stmt.setInt(5, availableCopies);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void editBookTitle(String bookId, String newTitle) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE BOOK SET title = ? WHERE id = ?"
            );
            stmt.setString(1, newTitle);
            stmt.setString(2, bookId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void editBookAuthor(String bookId, String newAuthor) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE BOOK SET author = ? WHERE id = ?"
            );
            stmt.setString(1, newAuthor);
            stmt.setString(2, bookId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void editBookGenre(String bookId, String newGenre) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE BOOK SET genre = ? WHERE id = ?"
            );
            stmt.setString(1, newGenre);
            stmt.setString(2, bookId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void editBookAvailableCopies(String bookId, int newCopies) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE BOOK SET available_copies = ? WHERE id = ?"
            );
            stmt.setInt(1, newCopies);
            stmt.setString(2, bookId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteBook(String bookId) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM BOOK WHERE id = ?"
            );
            stmt.setString(1, bookId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addBorrow(String userId, String bookId) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO BORROWS (user_id, book_id) VALUES (?, ?)"
            );
            stmt.setString(1, userId);
            stmt.setString(2, bookId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteBorrow(String userId, String bookId) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM BORROWS WHERE user_id = ? AND book_id = ?"
            );
            stmt.setString(1, userId);
            stmt.setString(2, bookId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findUserByName(String name, String roleNeeded) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM USER WHERE name = ?"
            );
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String id = rs.getString("id");
                String role = rs.getString("role").toLowerCase();

                if (!role.equals(roleNeeded)) {
                    throw new RuntimeException();
                }

                List<Book> borrowedBooks = getBorrowedBooks(id);

                if (role.equals("admin")) {
                    return new Admin(id, name, borrowedBooks);
                } else {
                    return new RegularUser(id, name, borrowedBooks);
                }
            } else {
                return null; // User not found
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Book> getBorrowedBooks(String userId) {
        List<Book> borrowedBooks = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT b.id, b.title, b.author, b.genre, b.available_copies " +
                            "FROM BOOK b JOIN BORROWS br ON b.id = br.book_id " +
                            "WHERE br.user_id = ?"
            );
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Book book = new Book(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("genre"),
                        String.valueOf(rs.getInt("available_copies"))
                );
                borrowedBooks.add(book);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return borrowedBooks;
    }

    public Book findBookByTitle(String title) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM BOOK WHERE title = ?"
            );
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Book(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("genre"),
                        String.valueOf(rs.getInt("available_copies"))
                );
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
