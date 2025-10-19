package org.example.service.impl;

import org.example.models.Book;
import org.example.service.BookManagementService;
import org.example.service.InventoryManagementService;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class BookManagementServiceImpl implements BookManagementService {
    private final InventoryManagementService inventoryManagementService;
    public BookManagementServiceImpl(InventoryManagementService inventoryManagementService) {
        this.inventoryManagementService = inventoryManagementService;
    }
    @Override
    public String addBooks(List<Book> books) {
        return inventoryManagementService.addBooks(books);
    }

    @Override
    public String updateBooks(List<Book> books) {
        return inventoryManagementService.updateBooks(books);
    }

    @Override
    public String removeBooks(List<Long> bookIds) {
        return inventoryManagementService.removeBooks(bookIds);
    }

    @Override
    public List<Book> searchBooks(String title, String author) {
        Collection<Book> books = InventoryManagementService.books.values();
        return books.stream().filter(book -> Objects.equals(book.getTitle(), title) || Objects.equals(book.getAuthor(), author)).toList();
    }
}
