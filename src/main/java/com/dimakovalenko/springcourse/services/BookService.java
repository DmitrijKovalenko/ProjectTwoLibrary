package com.dimakovalenko.springcourse.services;

import com.dimakovalenko.springcourse.models.Book;
import com.dimakovalenko.springcourse.models.Person;
import com.dimakovalenko.springcourse.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findBookById(int id) {
        Optional<Book> foundBook = bookRepository.findById(id);
        return foundBook.orElse(null);
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void update(Book updatedBook, int id) {
        updatedBook.setId(id);
        bookRepository.save(updatedBook);

    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public void release(int id) {
        bookRepository.findById(id).ifPresent(book -> {
            book.setOwner(null);
            book.setTakenAt(null);
        });
    }

    @Transactional
    public void assign(int id, Person selectedPerson) {
        bookRepository.findById(id).ifPresent(book -> {
            book.setOwner(selectedPerson);

        });
        bookRepository.findById(id).ifPresent(book -> book.setTakenAt(new Date()));

    }

    @Transactional
    public Person getBookOwner(int id) {
        Person bookOwner = bookRepository.findById(id).map(Book::getOwner).orElse(null);
        return bookOwner;
    }

    public List<Book> findWithPagination(Integer page, Integer booksPerPage, boolean sortByYear) {
        if (sortByYear) {
            return bookRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
        } else {
            return bookRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
        }

    }

    public List<Book> findAll(boolean sortByYear) {
        if (sortByYear)
            return bookRepository.findAll(Sort.by("year"));
        else
            return bookRepository.findAll();
    }

    public List<Book> findByTitleStartingWith(String title) {
        return bookRepository.findByTitleStartingWithIgnoreCase(title);
    }

    public void checkOverdue(int id) {
        Date takenAt = bookRepository.findById(id).get().getTakenAt();
        if(takenAt!=null){
            LocalDateTime localDateTime = takenAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            localDateTime = localDateTime.plusDays(1);
            Date datePlus10Days = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            Date now = new Date();
            if(now.toInstant().isAfter(datePlus10Days.toInstant())){
                bookRepository.findById(id).get().setOverdue(true);
            }
        }
    }
}
