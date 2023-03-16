package com.dimakovalenko.springcourse.controllers;
import com.dimakovalenko.springcourse.models.Person;
import com.dimakovalenko.springcourse.services.BookService;
import com.dimakovalenko.springcourse.services.PeopleServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleServices peopleServices;
    private final BookService bookService;

    @Autowired
    public PeopleController(PeopleServices peopleServices, BookService bookService) {
        this.peopleServices = peopleServices;

        this.bookService = bookService;
    }
    @GetMapping
    public String index(Model model){
        model.addAttribute("people",peopleServices.findAll());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id,Model model){
        model.addAttribute("person",peopleServices.findOne(id));
        model.addAttribute("books",peopleServices.getBooksByPersonId(id));
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person){
    return "people/new";
    }

    @PostMapping
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "people/new";
        }
        peopleServices.save(person);
        return "redirect:/people";
    }
    @GetMapping("/{id}/edit")
    public String edit(Model model,@PathVariable("id") int id){
        model.addAttribute("person",peopleServices.findOne(id));
        return "people/edit";
    }
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person,BindingResult bindingResult,
                         @PathVariable("id") int id){
        if(bindingResult.hasErrors()){
            return "people/new";
        }
        peopleServices.update(person,id);
        return "redirect:/people";
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        peopleServices.delete(id);
        return "redirect:/people";
    }




}
