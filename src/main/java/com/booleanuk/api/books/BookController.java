package com.booleanuk.api.books;

import com.booleanuk.api.authors.Author;
import com.booleanuk.api.authors.AuthorRepository;
import com.booleanuk.api.publishers.Publisher;
import com.booleanuk.api.publishers.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("books")
public class BookController {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private PublisherRepository publisherRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Book> getAllBooks() {
        return this.bookRepository.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public Book getOneBook(@PathVariable int id) {
        return this.bookRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Book createBook(@RequestBody Book book) {
        Author authorRef = this.authorRepository.findById(book.getAuthor().getId()).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found.")
        );
        Publisher publisherRef = this.publisherRepository.findById(book.getPublisher().getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Publisher not found.")
        );

        Book newBook = new Book(book.getTitle(), book.getGenre());
        newBook.setAuthor(authorRef);
        newBook.setPublisher(publisherRef);
        return this.bookRepository.save(newBook);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("{id}")
    public Book updateBook(@PathVariable int id, @RequestBody Book book) {
        Book bookToUpdate = this.bookRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found")
        );

        Author authorRef = this.authorRepository.findById(book.getAuthor().getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found.")
        );

        Publisher publisherRef = this.publisherRepository.findById(book.getPublisher().getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Publisher not found.")
        );

        bookToUpdate.setTitle(book.getTitle());
        bookToUpdate.setGenre(book.getGenre());
        bookToUpdate.setAuthor(authorRef);
        bookToUpdate.setPublisher(publisherRef);
        return this.bookRepository.save(bookToUpdate);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("{id}")
    public Book deleteBook(@PathVariable int id) {
        Book bookToDelete = this.bookRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found.")
        );

        this.bookRepository.delete(bookToDelete);
        return bookToDelete;
    }
}
