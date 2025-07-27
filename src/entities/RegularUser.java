package entities;

import storage.StorageManager;

import java.util.List;

public class RegularUser extends User implements Borrowable{
    public RegularUser(String id, String name, List<Book> borrowedBooks) {
        super(id, name, borrowedBooks);
    }

    private void incCopies(String bookId) {
        StorageManager sm = StorageManager.getStorageManager();
        int copies = sm.getAvailableCopiesByBookId(bookId);
        sm.editBookAvailableCopies(bookId, copies+1);
    }

    private void decCopies(String bookId) {
        StorageManager sm = StorageManager.getStorageManager();
        int copies = sm.getAvailableCopiesByBookId(bookId);
        sm.editBookAvailableCopies(bookId, copies-1);
    }

    @Override
    public void borrowBook(String bookId) {
        StorageManager sm = StorageManager.getStorageManager();
        sm.addBorrow(getId(), bookId);
        decCopies(bookId);
    }

    @Override
    public void returnBook(String bookId) {
        StorageManager sm = StorageManager.getStorageManager();
        if (sm.deleteBorrow(getId(), bookId)!=0)
            incCopies(bookId);
        else {
            throw new RuntimeException("Borrow already exists");
        }
    }

    public List<Book> getAllBooks() {
        StorageManager sm = StorageManager.getStorageManager();
        return sm.getAllBooks();
    }
}
