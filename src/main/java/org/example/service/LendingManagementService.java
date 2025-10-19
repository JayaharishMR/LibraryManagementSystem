package org.example.service;

import org.example.models.Book;
import org.example.models.Patron;

public interface LendingManagementService {
    Book getBookdetails(Long bookId);
    Patron getPatronDetails(Long patronId);
    String checkoutBook (Long bookId, Long patronId);
    String returnBook (Long bookId, Long patronId);
}
