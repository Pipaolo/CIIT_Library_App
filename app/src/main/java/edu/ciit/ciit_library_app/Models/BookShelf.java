package edu.ciit.ciit_library_app.Models;

public class BookShelf {
    private String description;
    private String title;
    private String genre;
    private int id;

    public BookShelf() {
    }

    public BookShelf(String bookTitle, String bookGenre, int id) {
        this.title = bookTitle;
        this.genre = bookGenre;
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
