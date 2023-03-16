package com.dimakovalenko.springcourse.services;

import com.dimakovalenko.springcourse.models.Book;
import com.dimakovalenko.springcourse.models.Person;
import com.dimakovalenko.springcourse.repositories.BookRepository;
import com.dimakovalenko.springcourse.repositories.PeopleRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleServices {

    private final PeopleRepository peopleRepository;
    private final BookService bookService;

    @Autowired
    public PeopleServices(PeopleRepository peopleRepository, BookService bookService) {
        this.peopleRepository = peopleRepository;
        this.bookService = bookService;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findOne(int id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);
        return foundPerson.orElse(null);
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(Person updatedPerson, int id) {
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public Optional<Person> getPersonByFullName(String fulName) {
        Optional<Person> foundPerson;
        return foundPerson = peopleRepository.findPersonByFullName(fulName);
    }

    public List<Book> getBooksByPersonId(int id) {
        Optional<Person> person = peopleRepository.findById(id);
        if (person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());
                 person.get().getBooks().forEach(book -> {
                     int toCheck = book.getId();
                     bookService.checkOverdue(toCheck);
                 });
            return person.get().getBooks();
        } else {
            return Collections.emptyList();
        }
    }


}
