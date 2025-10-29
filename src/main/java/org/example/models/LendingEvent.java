package org.example.models;

import lombok.*;
import org.example.constants.LendingEventType;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LendingEvent {
    private Long bookId;
    private Long patronId;
    private LocalDateTime localDateTime;
    private LendingEventType lendingEventType;
}
