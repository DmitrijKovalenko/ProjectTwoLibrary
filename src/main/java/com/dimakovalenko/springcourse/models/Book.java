package com.dimakovalenko.springcourse.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "Book")
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    @NotEmpty(message = "Название кники не должно быть пустым")
    @Size(min = 2, max = 150, message = "Название книги должно быть от 2 до 150 символов")
    private String title;

    @Column(name = "author")
    @NotEmpty(message = "Имя автора не должно быть пустым")
    @Size(min = 2, max = 150, message = "Имя автора должно быть от 2 до 150 символов")
    private String author;

    @Column(name = "year")
    @Min(value = 1500, message = "Год не должен быть меньше 1500")
    private int year;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;
    @Column(name = "taken_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date takenAt;

    @Transient
    private boolean overdue;

    public Book() {
    }

    public Book(String title, String author, int year, Date takenAt, boolean overdue) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.takenAt = takenAt;
        this.overdue = overdue;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Date getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(Date takenAt) {
        this.takenAt = takenAt;
    }

    public boolean isOverdue() {
        return overdue;
    }

    public void setOverdue(boolean overdue) {
        this.overdue = overdue;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", year=" + year +
                ", owner=" + owner +
                ", takenAt=" + takenAt +
                ", overdue=" + overdue +
                '}';
    }
}
