package org.example.models;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Book {
    private Long bookId;
    private String title;
    private String genre;
    private String isbn;
    private String author;
    private String publication;
    private String publicationYear;
    private int noOfCopies;
    private int availableBooks;
    private List<Long> lendedOutPatronIds;
    private LocalDateTime createdTs;
    private LocalDateTime lastUpdatedTs;
}
