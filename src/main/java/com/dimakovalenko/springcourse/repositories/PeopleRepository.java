package com.dimakovalenko.springcourse.repositories;

import com.dimakovalenko.springcourse.models.Book;
import com.dimakovalenko.springcourse.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PeopleRepository extends JpaRepository<Person,Integer> {
    public Optional<Person> findPersonByFullName(String name);
}
