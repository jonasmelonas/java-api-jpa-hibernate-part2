package com.booleanuk.api.publishers;

import com.booleanuk.api.books.Book;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "publishers")
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Column
    private String location;

    @OneToMany(mappedBy = "publisher")
    @JsonIncludeProperties({"title", "genre"})
    private List<Book> books;

    public Publisher(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public Publisher(int id) {
        this.id = id;
    }
}
