package com.dimakovalenko.springcourse.util;

import com.dimakovalenko.springcourse.models.Person;
import com.dimakovalenko.springcourse.services.PeopleServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {
    private final PeopleServices peopleServices;
    @Autowired
    public PersonValidator( PeopleServices peopleServices) {
        this.peopleServices = peopleServices;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if(peopleServices.getPersonByFullName(person.getFullName()).isPresent()){
            errors.rejectValue("fullName","","Человек с таким именем уже существует");
        }
    }

}
