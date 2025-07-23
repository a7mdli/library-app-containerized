package entities;

public class Book {
    private String id;
    private String title;
    private String author;
    private String genre;
    private String availableCopies;

    public Book(String id, String title, String author, String genre, String availableCopies) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.availableCopies = availableCopies;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public String getAvailableCopies() {
        return availableCopies;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setAvailableCopies(String availableCopies) {
        this.availableCopies = availableCopies;
    }
}
