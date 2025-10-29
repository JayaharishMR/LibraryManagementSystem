package org.example;

import org.example.models.Book;
import org.example.models.Patron;
import org.example.service.BookManagementService;
import org.example.service.InventoryManagementService;
import org.example.service.LendingManagementService;
import org.example.service.PatronManagementService;
import org.example.service.impl.BookManagementServiceImpl;
import org.example.service.impl.InventoryManagementServiceImpl;
import org.example.service.impl.LendingManagementServiceImpl;
import org.example.service.impl.PatronMangementServiceImpl;

import java.time.LocalDateTime;
import java.util.*;

public class App {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        InventoryManagementService inventoryService = new InventoryManagementServiceImpl();
        PatronManagementService patronService = new PatronMangementServiceImpl();
        BookManagementService bookService = new BookManagementServiceImpl(inventoryService);
        LendingManagementService lendingService = new LendingManagementServiceImpl(inventoryService, patronService);

        InventoryManagementService.books = new HashMap<>();
        InventoryManagementService.availableBooks = new HashSet<>();
        InventoryManagementService.borrowedBooks = new HashSet<>();
        PatronMangementServiceImpl.patronStore = new HashMap<>();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> addBook(bookService);
                case 2 -> addPatron(patronService);
                case 3 -> searchBooks(bookService);
                case 4 -> checkoutBook(lendingService);
                case 5 -> returnBook(lendingService);
                case 6 -> removeBook(bookService);
                case 7 -> viewInventory(inventoryService);
                case 8 -> viewLibrarySummary(inventoryService);
                case 9 -> viewBookDetails(inventoryService);
                case 10 -> viewPatronDetails(patronService);
                case 11 -> updatePatron(patronService);
                case 0 -> {
                    System.out.println("Exiting program...");
                    running = false;
                }
                default -> System.out.println("Invalid choice! Please try again.");
            }
            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println("===== Library Management Menu =====");
        System.out.println("1. Add Book");
        System.out.println("2. Add Patron");
        System.out.println("3. Search Books");
        System.out.println("4. Checkout Book");
        System.out.println("5. Return Book");
        System.out.println("6. Remove Book");
        System.out.println("7. View Inventory");
        System.out.println("8. Library Summary");
        System.out.println("9. View Book Details");
        System.out.println("10. View Patron Details");
        System.out.println("11. Update Patron Details");
        System.out.println("0. Exit");
    }

    private static int readInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. " + prompt);
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static void addBook(BookManagementService bookService) {
        Long id = (long) readInt("Enter book ID: ");
        String title = readString("Enter title: ");
        String author = readString("Enter author: ");
        String genre = readString("Enter genre: ");
        String pubYear = readString("Enter publication year: ");
        int copies = readInt("Enter number of copies: ");

        Book book = new Book(
                id, title, genre, null, author, null, pubYear,
                copies, copies, new ArrayList<>(), LocalDateTime.now(), null
        );
        System.out.println(bookService.addBooks(List.of(book)));
    }

    private static void addPatron(PatronManagementService patronService) {
        Long id = (long) readInt("Enter patron ID: ");
        String name = readString("Enter patron name: ");

        Patron patron = new Patron(id, name, new ArrayList<>(), LocalDateTime.now(), null);
        System.out.println(patronService.addPatrons(List.of(patron)));
    }

    private static void updatePatron(PatronManagementService patronService) {
        Long id = (long) readInt("Enter patron ID to update: ");
        Patron existing = patronService.getPatronDetails(id);

        if (existing == null) {
            System.out.println("No patron found for ID: " + id);
            return;
        }

        String name = readString("Enter new name (leave blank to keep unchanged): ");
        if (!name.isEmpty()) {
            existing.setName(name);
        }
        existing.setLastUpdatedTs(LocalDateTime.now());

        System.out.println(patronService.updatePatrons(List.of(existing)));
    }

    private static void searchBooks(BookManagementService bookService) {
        String title = readString("Enter title (or leave blank): ");
        String author = readString("Enter author (or leave blank): ");

        List<Book> results = bookService.searchBooks(
                title.isEmpty() ? null : title,
                author.isEmpty() ? null : author
        );
        if (results.isEmpty()) {
            System.out.println("No books found.");
        } else {
            results.forEach(System.out::println);
        }
    }

    private static void checkoutBook(LendingManagementService lendingService) {
        Long bookId = (long) readInt("Enter book ID to checkout: ");
        Long patronId = (long) readInt("Enter patron ID: ");
        System.out.println(lendingService.checkoutBook(bookId, patronId));
    }

    private static void returnBook(LendingManagementService lendingService) {
        Long bookId = (long) readInt("Enter book ID to return: ");
        Long patronId = (long) readInt("Enter patron ID: ");
        System.out.println(lendingService.returnBook(bookId, patronId));
    }

    private static void removeBook(BookManagementService bookService) {
        Long bookId = (long) readInt("Enter book ID to remove: ");
        System.out.println(bookService.removeBooks(List.of(bookId)));
    }

    private static void viewInventory(InventoryManagementService inventoryService) {
        inventoryService.viewInventory();
    }

    private static void viewLibrarySummary(InventoryManagementService inventoryManagementService) {
        inventoryManagementService.viewLibrarySummary();
    }

    private static void viewBookDetails(InventoryManagementService inventoryService) {
        Long bookId = (long) readInt("Enter book ID: ");
        Book book = inventoryService.getBookDetails(bookId);
        if (book == null) {
            System.out.println("No book found for ID: " + bookId);
        } else {
            System.out.println("Book Details:");
            System.out.println(book);
        }
    }

    private static void viewPatronDetails(PatronManagementService patronService) {
        Long patronId = (long) readInt("Enter patron ID: ");
        Patron patron = patronService.getPatronDetails(patronId);
        if (patron == null) {
            System.out.println("No patron found for ID: " + patronId);
        } else {
            System.out.println("Patron Details:");
            System.out.println(patron);
        }
    }
}
