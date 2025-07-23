package entities;

import storage.StorageManager;

import java.util.List;
import java.util.UUID;

public class Admin extends User{
    public Admin(String id, String name, List<Book> borrowedBooks) {
        super(id, name, borrowedBooks);
    }

    public void addUser(String name, Role role) {
        StorageManager sm = StorageManager.getStorageManager();
        sm.addUser(UUID.randomUUID().toString(),name, role==Role.ADMIN ? "admin" : "regular");
    }

    public void addBook(String title, String author, String genre, int availableCopies) {
        StorageManager sm = StorageManager.getStorageManager();
        String bookId = UUID.randomUUID().toString();
        sm.addBook(bookId, title, author, genre, availableCopies);
    }

    public void editBookTitle(String bookId, String newTitle) {
        StorageManager sm = StorageManager.getStorageManager();
        sm.editBookTitle(bookId, newTitle);
    }

    public void editBookAuthor(String bookId, String newAuthor) {
        StorageManager sm = StorageManager.getStorageManager();
        sm.editBookAuthor(bookId, newAuthor);
    }

    public void editBookGenre(String bookId, String newGenre) {
        StorageManager sm = StorageManager.getStorageManager();
        sm.editBookGenre(bookId, newGenre);
    }

    public void editBookAvailableCopies(String bookId, int newCopies) {
        StorageManager sm = StorageManager.getStorageManager();
        sm.editBookAvailableCopies(bookId, newCopies);
    }

    public void deleteBook(String bookId) {
        StorageManager sm = StorageManager.getStorageManager();
        sm.deleteBook(bookId);
    }
}
