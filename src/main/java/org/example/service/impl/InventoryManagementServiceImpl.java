package org.example.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.example.constants.LendingEventType;
import org.example.models.Book;
import org.example.service.InventoryManagementService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class InventoryManagementServiceImpl extends InventoryManagementService {
    @Override
    public Book getBookDetails(Long bookId) {
        if (!books.containsKey(bookId)) {
            log.info("Book is not available for id {} while removing", bookId);
            return null;
        }
        return books.get(bookId);
    }

    @Override
    public String addBooks(List<Book> bookList) {
        for (Book book : bookList) {
            if (InventoryManagementService.books.containsKey(book.getBookId())) {
                log.info("Book is already available with id {} and title {}, so skipping the addition", book.getBookId(), book.getTitle());
                continue;
            }
            book.setCreatedTs(LocalDateTime.now());
            InventoryManagementService.books.put(book.getBookId(), book);
            availableBooks.add(book.getBookId());
        }
        return "Successfully added the books in the inventory";
    }

    @Override
    public String updateBooks(List<Book> bookList) {
        for (Book book : bookList) {
            if (!InventoryManagementService.books.containsKey(book.getBookId())) {
                log.info("Book is not available for id {} and title {}", book.getBookId(), book.getTitle());
                continue;
            }
            book.setLastUpdatedTs(LocalDateTime.now());
            InventoryManagementService.books.put(book.getBookId(), book);
        }
        return "Successfully updated the books in the inventory";
    }

    @Override
    public String removeBooks(List<Long> bookIds) {
        for (Long bookId : bookIds) {
            if (!books.containsKey(bookId)) {
                log.info("Book is not available for id {} while removing", bookId);
                continue;
            }
            if (borrowedBooks.contains(bookId)) {
                log.info("Books with id {} has been borrowed, hence cannot remove the book", bookId);
                continue;
            }
            books.remove(bookId);
            removeAvailableBooks(bookId);
        }
        return "Successfully removed the books from the inventory";
    }

    @Override
    public String updateOnLendingEvent(Long bookId, Long patronId, LendingEventType eventType) {
        if (eventType.equals(LendingEventType.CHECKED_OUT)) {
            checkOutEvent(bookId, patronId);
            return "Processed check out event in the Inventory";
        }
        if (eventType.equals(LendingEventType.RETURN)) {
            returnEvent(bookId, patronId);
            return "Processed return event in the Inventory";
        }
        return "Event type is not configured in Inventory";
    }

    @Override
    public boolean isBookAvailable(Long bookId) {
        return InventoryManagementService.availableBooks.contains(bookId);
    }

    @Override
    public boolean isBookBorrowed(Long bookId) {
        return InventoryManagementService.borrowedBooks.contains(bookId);
    }

    private void addAvailableBooks(Long bookId) {
        availableBooks.add(bookId);
    }

    private void addBorrowedBooks(Long bookId) {
        borrowedBooks.add(bookId);
    }

    private void removeAvailableBooks(Long bookId) {
        availableBooks.remove(bookId);
    }

    private void removeBorrowedBooks(Long bookId) {
        borrowedBooks.remove(bookId);
    }

    private void checkOutEvent(Long bookId, Long patronId) {
        Book book = books.get(bookId);
        book.setAvailableBooks(book.getAvailableBooks()-1);
        book.getLendedOutPatronIds().add(patronId);
        book.setLastUpdatedTs(LocalDateTime.now());
        if (book.getAvailableBooks() == 0 ){
            removeAvailableBooks(bookId);
        }
        if (!borrowedBooks.contains(bookId)) {
            addBorrowedBooks(bookId);
        }
    }

    private void returnEvent(Long bookId, Long patornId) {
        Book book = books.get(bookId);
        if (!book.getLendedOutPatronIds().contains(patornId)) {
            log.info("The patron is not one of the borrowers");
        } else {
            book.getLendedOutPatronIds().remove(patornId);
            book.setAvailableBooks(book.getAvailableBooks()+1);
            if (!availableBooks.contains(bookId)) addAvailableBooks(bookId);
            if (book.getNoOfCopies() == book.getAvailableBooks()) removeBorrowedBooks(bookId);
        }
    }

    @Override
    public void viewLibrarySummary() {
        int totalCopies = InventoryManagementService.books.values().stream()
                .mapToInt(book -> book.getNoOfCopies())
                .sum();

        int totalAvailable = InventoryManagementService.books.values().stream()
                .mapToInt(book -> book.getAvailableBooks())
                .sum();

        System.out.println("=== Library Summary ===");
        System.out.println("Total copies: " + totalCopies);
        System.out.println("Total available books: " + totalAvailable);
    }

    @Override
    public void viewInventory() {
        System.out.println("=== Current Inventory ===");
        InventoryManagementService.books.values().forEach(System.out::println);
    }
}
