package org.example.models;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Patron {
    private Long patronId;
    private String name;
    private List<BorrowedBookHistroy> borrowedBookHistroyList;
    private LocalDateTime createTs;
    private LocalDateTime lastUpdatedTs;
}
