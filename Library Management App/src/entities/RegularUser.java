package entities;

import storage.StorageManager;

import java.util.List;

public class RegularUser extends User implements Borrowable{
    public RegularUser(String id, String name, List<Book> borrowedBooks) {
        super(id, name, borrowedBooks);
    }

    @Override
    public void borrowBook(String bookId) {
        StorageManager sm = StorageManager.getStorageManager();
        sm.addBorrow(getId(), bookId);
    }

    @Override
    public void returnBook(String bookId) {
        StorageManager sm = StorageManager.getStorageManager();
        sm.deleteBorrow(getId(), bookId);
    }
}
