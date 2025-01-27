package com.booleanuk.api.authors;

import com.booleanuk.api.books.Book;
import com.booleanuk.api.books.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("authors")
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Author> getAllAuthors() {
        return this.authorRepository.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public Author getOneAuthor(@PathVariable int id) {
        return this.authorRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Author createAuthor(@RequestBody Author author) {
        Author newAuthor = new Author(author.getFirstName(), author.getLastName(), author.getEmail(), author.isAlive());
        return this.authorRepository.save(newAuthor);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("{id}")
    public Author updateAuthor(@PathVariable int id, @RequestBody Author author) {
        Author authorToUpdate = this.authorRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found")
        );

        authorToUpdate.setFirstName(author.getFirstName());
        authorToUpdate.setLastName(author.getLastName());
        authorToUpdate.setEmail(author.getEmail());
        authorToUpdate.setAlive(author.isAlive());
        return this.authorRepository.save(authorToUpdate);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("{id}")
    public Author deleteAuthor(@PathVariable int id) {
        Author authorToDelete = this.authorRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found.")
        );

        this.bookRepository.deleteAll(authorToDelete.getBooks());
        this.authorRepository.delete(authorToDelete);
        return authorToDelete;
    }
}
