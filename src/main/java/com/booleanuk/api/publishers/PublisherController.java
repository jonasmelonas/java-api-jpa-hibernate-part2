package com.booleanuk.api.publishers;

import com.booleanuk.api.books.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("publishers")
public class PublisherController {
    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private BookRepository bookRepository;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Publisher> getAllPublishers() {
        return this.publisherRepository.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public Publisher getOnePublisher(@PathVariable int id) {
        return this.publisherRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Publisher createPublisher(@RequestBody Publisher publisher) {
        Publisher newPublisher = new Publisher(publisher.getName(), publisher.getLocation());
        return this.publisherRepository.save(newPublisher);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("{id}")
    public Publisher updatePublisher(@PathVariable int id, @RequestBody Publisher publisher) {
        Publisher publisherToUpdate = this.publisherRepository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Publisher not found")
        );

        publisherToUpdate.setName(publisher.getName());
        publisherToUpdate.setLocation(publisher.getLocation());
        return this.publisherRepository.save(publisherToUpdate);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("{id}")
    public Publisher deletePublisher(@PathVariable int id) {
        Publisher publisherToDelete = this.publisherRepository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Publisher not found.")
        );

        this.bookRepository.deleteAll(publisherToDelete.getBooks());
        this.publisherRepository.delete(publisherToDelete);
        return publisherToDelete;
    }
}
