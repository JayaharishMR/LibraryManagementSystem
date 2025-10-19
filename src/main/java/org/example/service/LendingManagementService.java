package org.example.service;

import org.example.models.Book;
import org.example.models.Patron;

public interface LendingManagementService {
    String checkoutBook (Long bookId, Long patronId);
    String returnBook (Long bookId, Long patronId);
}
