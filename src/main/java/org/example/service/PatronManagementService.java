package org.example.service;

import org.example.constants.LendingEventType;
import org.example.exception.validatorException;
import org.example.models.Patron;

import java.util.List;

public interface PatronManagementService {
    String addPatrons(List<Patron> patronList);
    String updatePatrons(List<Patron> patronList);
    String updateBorrowedDetails(Long bookId, Long patronId, LendingEventType eventType) throws validatorException;
    Patron getPatronDetails(Long patronId);
    void validateCheckoutRequest(Long bookId, Long patronId) throws validatorException;
}
