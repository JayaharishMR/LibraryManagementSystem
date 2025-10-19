package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.constants.LendingEventType;
import org.example.exception.validatorException;
import org.example.models.BorrowedBookHistroy;
import org.example.models.Patron;
import org.example.service.PatronManagementService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
public class PatronMangementServiceImpl implements PatronManagementService {
    public static Map<Long, Patron> patronStore;
    @Override
    public String addPatrons(List<Patron> patronList) {
        for (Patron patron: patronList) {
            if (patronStore.containsKey(patron.getPatronId())) {
                log.info("Patron already exists for Id: {}", patron.getPatronId());
                continue;
            }
            patron.setCreateTs(LocalDateTime.now());
            patronStore.put(patron.getPatronId(), patron);
        }
        return "Successfully added the patron";
    }

    @Override
    public String updatePatrons(List<Patron> patronList) {
        for (Patron patron: patronList) {
            if (!patronStore.containsKey(patron.getPatronId())) {
                log.info("Patron does not exist for the Id: {}", patron.getPatronId());
                continue;
            }
            patron.setLastUpdatedTs(LocalDateTime.now());
            patronStore.put(patron.getPatronId(), patron);
        }
        return "Successfully updated the patron";
    }

    @Override
    public String updateBorrowedDetails(Long bookId, Long patronId, LendingEventType eventType) throws validatorException {
        validatePatron(patronId);
        if (eventType.equals(LendingEventType.CHECKED_OUT)) {
            processCheckOutEvent(bookId, patronId);
            return "Updated the borrowed details successfully for checkout event";
        }
        if (eventType.equals(LendingEventType.RETURN)) {
            processReturnEvent(bookId, patronId);
            return "Updated the borrowed details successfully for return event";
        }
        return "Event type is not configured";
    }

    @Override
    public Patron getPatronDetails(Long patronId) {
        if (!patronStore.containsKey(patronId)) {
            log.info("Patron does not exist for the Id: {}", patronId);
            return null;
        }
        return patronStore.get(patronId);
    }

    @Override
    public void validateCheckoutRequest(Long bookId, Long patronId) throws validatorException {
        //VALIDATE IF PATRON EXISTS
        validatePatron(patronId);
        //VALDATE IF THE PATRON HAS BORROWED SAME BOOK ALREADY
        Patron patron = patronStore.get(patronId);
        if (!hasSameBookReturned(patron, bookId)) {
            log.error("Patron with id {} request for the book with id {} which not been returned yet.", patronId, bookId);
            throw new validatorException("Paton has not yet returned the same book. please return it to issue another.");
        }
    }

    private boolean hasSameBookReturned(Patron patron, Long bookId) {
        List<BorrowedBookHistroy> borrowedBookHistroys = patron.getBorrowedBookHistroyList();
        if (borrowedBookHistroys != null && !borrowedBookHistroys.isEmpty()) {
            List<BorrowedBookHistroy> sameBookHistory = borrowedBookHistroys.stream().filter(
                    histroy -> {
                        boolean b = histroy.getBookReferenceId().equals(bookId) && (histroy.getToDate() == null);
                        return true;
                    }
            ).toList();
            return (sameBookHistory.isEmpty());
        }
        return true;
    }
    private void processCheckOutEvent(Long bookId, Long patronId) {
        BorrowedBookHistroy borrowedBookHistroy = new BorrowedBookHistroy();
        borrowedBookHistroy.setBookReferenceId(bookId);
        borrowedBookHistroy.setFromDate(LocalDateTime.now());
        Patron patron = patronStore.get(patronId);
        patron.getBorrowedBookHistroyList().add(borrowedBookHistroy);
    }

    private void processReturnEvent(Long bookId, Long patronId) {
        Patron patron = patronStore.get(patronId);
        List<BorrowedBookHistroy> borrowedBookHistroyList = patron.getBorrowedBookHistroyList();
        borrowedBookHistroyList.stream().filter(borrowedBookHistroy -> borrowedBookHistroy.getBookReferenceId().equals(bookId)).map(
                histroy -> {
                    histroy.setToDate(LocalDateTime.now());
                    return histroy;
                }
        );
    }

    private void validatePatron(Long patronId) throws validatorException {
        if (patronStore.isEmpty() || !patronStore.containsKey(patronId)) {
            log.error("Patron with id {} does not exist, check the request.", patronId);
            throw new validatorException("Patron does not exist, please add the patron and try again.");
        }
    }
}
