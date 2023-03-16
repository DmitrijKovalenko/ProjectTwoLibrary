package com.dimakovalenko.springcourse.controllers;

import com.dimakovalenko.springcourse.models.Book;
import com.dimakovalenko.springcourse.models.Person;
import com.dimakovalenko.springcourse.repositories.BookRepository;
import com.dimakovalenko.springcourse.services.BookService;
import com.dimakovalenko.springcourse.services.PeopleServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final PeopleServices peopleServices;

    @Autowired
    public BookController(BookService bookService, PeopleServices peopleServices) {
        this.bookService = bookService;
        this.peopleServices = peopleServices;
    }

    @GetMapping
    public String index(@RequestParam(value = "page",required = false)Integer page,
                        @RequestParam(value = "books_per_page",required = false)Integer booksPerPage,
                        @RequestParam(value = "sor_by_year",required = false)boolean sortByYear, Model model) {
        if(page == null || booksPerPage ==null){
            model.addAttribute("books", bookService.findAll(sortByYear));
        }else {
           model.addAttribute("books", bookService.findWithPagination(page,booksPerPage,sortByYear));
        }
        return "book/index";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "book/new";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", bookService.findBookById(id));
        Person bookOwner = bookService.getBookOwner(id);
        if (bookOwner!=null) {
            model.addAttribute("owner", bookOwner);
        } else {
            model.addAttribute("people", peopleServices.findAll());
        }
        return "book/show";
    }

    @PostMapping
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "book/new";
        } else {
            bookService.save(book);
        }
        return "redirect:/books";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "book/new";
        } else {
            bookService.update(book, id);
        }
        return "redirect:/books";
    }
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable int id,Model model){
        model.addAttribute("book",bookService.findBookById(id));
        return "book/edit";

    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        bookService.release(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id")int id,@ModelAttribute("person") Person selectedPerson){
        bookService.assign(id,selectedPerson);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String searchPage(){
        return "book/search";
    }

    @PostMapping("/search")
    public String search(Model model ,@RequestParam("searchBook") String searchBook){
       model.addAttribute("books", bookService.findByTitleStartingWith(searchBook));

        return "book/search";
    }


}
