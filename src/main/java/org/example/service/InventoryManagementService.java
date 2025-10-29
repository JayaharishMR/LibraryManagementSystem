package org.example.service;

import org.example.constants.LendingEventType;
import org.example.models.Book;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class InventoryManagementService {
    public static Map<Long,Book> books;
    public static Set<Long> availableBooks;
    public static Set<Long> borrowedBooks;
    public abstract Book getBookDetails(Long bookId);
    public abstract String addBooks(List<Book> bookList);
    public abstract String updateBooks(List<Book> bookList);
    public abstract String removeBooks(List<Long> bookIds);
    public abstract String updateOnLendingEvent(Long bookId, Long patronId, LendingEventType eventType);
    public abstract boolean isBookAvailable(Long bookId);
    public abstract boolean isBookBorrowed(Long bookId);
    public abstract void viewLibrarySummary();
    public abstract void viewInventory();
}


