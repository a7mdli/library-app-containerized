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

    private static Connection waitForConnection(String url, String user, String password) {
        int retries = 30;
        int delayMs = 2000;

        for (int i = 0; i < retries; i++) {
            try {
                Connection conn = DriverManager.getConnection(url, user, password);
                System.out.println("Connected to DB");
                return conn;
            } catch (SQLException e) {
                System.out.println("Waiting for DB... attempt " + (i + 1));
                try {
                    Thread.sleep(delayMs);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted while waiting for DB", ie);
                }
            }
        }
        throw new RuntimeException("Could not connect to DB after " + retries + " attempts");
    }


    private StorageManager() {
        String host = System.getenv("DB_HOST");
        String port = System.getenv("DB_PORT");
        String dbName = System.getenv("DB_NAME");
        DB_URL = "jdbc:mysql://"+host+":"+port+"/"+dbName;
        USER = System.getenv("DB_USER");
        PASSWORD = System.getenv("DB_PASSWORD");

        connection = waitForConnection(DB_URL, USER, PASSWORD);
    }

    public void addUser(String id, String name, String role) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO USER (id, name, role) VALUES (?, ?, ?)"
            );
            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.setString(3, role);
            stmt.executeUpdate();
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

    public int getAvailableCopiesByBookId(String bookId) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT available_copies FROM BOOK WHERE id = ?"
            );
            stmt.setString(1, bookId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("available_copies");
            } else {
                return -1; // Book not found
            }
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

    public int deleteBorrow(String userId, String bookId) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM BORROWS WHERE user_id = ? AND book_id = ?"
            );
            stmt.setString(1, userId);
            stmt.setString(2, bookId);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findUserBy(String key, String value) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM USER WHERE "+ key + " = ?"
            );
            stmt.setString(1, value);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String role = rs.getString("role").toLowerCase();

                List<Book> borrowedBooks = getBorrowedBooks(id);

                if (role.equals("admin")) {
                    return new Admin(id, name, borrowedBooks);
                } else {
                    return new RegularUser(id, name, borrowedBooks);
                }
            } else {
                return null;
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

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM BOOK");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Book book = new Book(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("genre"),
                        String.valueOf(rs.getInt("available_copies"))
                );
                books.add(book);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return books;
    }


}
