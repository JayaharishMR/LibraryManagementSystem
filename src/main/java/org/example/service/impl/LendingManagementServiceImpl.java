package org.example.service.impl;

import org.example.constants.LendingEventType;
import org.example.exception.validatorException;
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
    public String checkoutBook(Long bookId, Long patronId) {
        if (!inventoryManagementService.isBookAvailable(bookId)) {
            return "Sorry the book is not available now.";
        }
        try {
            validateCheckoutRequest(bookId, patronId);
            patronManagementService.updateBorrowedDetails(bookId, patronId, LendingEventType.CHECKED_OUT);
            inventoryManagementService.updateOnLendingEvent(bookId, patronId, LendingEventType.CHECKED_OUT);
            return "The checkout was processed successfully";
        } catch (validatorException validatorException) {
            return validatorException.getMessage();
        }
    }

    @Override
    public String returnBook(Long bookId, Long patronId) {
        if (!inventoryManagementService.isBookBorrowed(bookId)) {
            return "Sorry the book is not borrowed out.";
        }
        try {
            patronManagementService.updateBorrowedDetails(bookId, patronId, LendingEventType.RETURN);
            inventoryManagementService.updateOnLendingEvent(bookId, patronId, LendingEventType.RETURN);
            return "The return was processed successfully";
        } catch (validatorException validatorException) {
            return validatorException.getMessage();
        }
    }

    private void validateCheckoutRequest(Long bookId, Long patronId) throws validatorException {
        patronManagementService.validateCheckoutRequest(bookId, patronId);
    }
}
