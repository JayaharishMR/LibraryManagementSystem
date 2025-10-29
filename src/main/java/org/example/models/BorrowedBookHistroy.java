package org.example.models;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BorrowedBookHistroy {
    private Long bookReferenceId;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
}
