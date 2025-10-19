package org.example.service;

import org.example.models.Book;

import java.util.List;

public interface BookManagementService {
    String addBooks(List<Book> books);
    String updateBooks(List<Book> books);
    String removeBooks(List<Long> bookIds);
    List<Book> searchBooks(String title, String author);
}
