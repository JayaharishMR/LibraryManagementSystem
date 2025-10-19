package org.example.service.impl;

import org.example.constants.LendingEventType;
import org.example.models.Book;
import org.example.models.Patron;
import org.example.service.InventoryManagementService;
import org.example.service.LendingManagementService;
import org.example.service.PatronManagementService;

public class LendingManagementServiceImpl implements LendingManagementService {
    private final InventoryManagementService inventoryManagementService;
    private final PatronManagementService patronManagementService;

    public LendingManagementServiceImpl(InventoryManagementService inventoryManagementService, PatronManagementService patronManagementService) {
        this.inventoryManagementService = inventoryManagementService;
        this.patronManagementService = patronManagementService;
    }
    @Override
    public Book getBookdetails(Long bookId) {
        return inventoryManagementService.getBookDetails(bookId);
    }

    @Override
    public Patron getPatronDetails(Long patronId) {
        return patronManagementService.getPatronDetails(patronId);
    }

    @Override
    public String checkoutBook(Long bookId, Long patronId) {
        if (!inventoryManagementService.isBookAvailable(bookId)) {
            return "Sorry the book is not available now.";
        }
        patronManagementService.updateBorrowedDetails(bookId, patronId, LendingEventType.CHECKED_OUT);
        inventoryManagementService.updateOnLendingEvent(bookId, patronId, LendingEventType.CHECKED_OUT);
        return "The checkout was processed successfully";
    }

    @Override
    public String returnBook(Long bookId, Long patronId) {
        if (!inventoryManagementService.isBookBorrowed(bookId)) {
            return "Sorry the book is not borrowed out.";
        }
        patronManagementService.updateBorrowedDetails(bookId, patronId, LendingEventType.RETURN);
        inventoryManagementService.updateOnLendingEvent(bookId, patronId, LendingEventType.RETURN);
        return "The return was processed successfully";
    }
}
