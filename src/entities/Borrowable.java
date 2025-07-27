package entities;

public interface Borrowable {
    public void borrowBook(String bookId);
    public void returnBook(String bookId);
}
