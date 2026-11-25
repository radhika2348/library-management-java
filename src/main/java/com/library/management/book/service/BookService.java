package com.library.management.book.service;

import java.util.Locale;

import org.springframework.stereotype.Service;

import com.library.management.author.repository.AuthorRepository;
import com.library.management.book.dto.BookResponse;
import com.library.management.book.dto.TieBooksToAuthorsRequest;
import com.library.management.book.dto.UpdateBookRequest;
import com.library.management.book.model.BookDocument;
import com.library.management.book.repository.BookRepository;
import com.library.management.exception.BadRequestException;
import com.library.management.exception.ConflictException;
import com.library.management.exception.ResourceNotFoundException;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, BookMapper bookMapper, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.authorRepository = authorRepository;
    }

    public BookResponse update(String bookId, UpdateBookRequest request) {
        BookDocument book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + bookId));

        String normalizedIsbn = normalizeIsbn(request.isbn());

        if (bookRepository.existsByIsbnAndIdNot(normalizedIsbn, bookId)) {
            throw new ConflictException("ISBN is already assigned to another book");
        }

        if (request.availableCopies() > request.totalCopies()) {
            throw new BadRequestException("Available copies cannot exceed total copies");
        }

        book.setTitle(request.title().trim());
        book.setAuthorName(request.authorName().trim());
        book.setIsbn(normalizedIsbn);
        book.setDescription(request.description() == null ? null : request.description().trim());
        book.setPublicationYear(request.publicationYear());
        book.setTotalCopies(request.totalCopies());
        book.setAvailableCopies(request.availableCopies());

        BookDocument saved = bookRepository.save(book);
        return bookMapper.toResponse(saved);
    }

    private String normalizeIsbn(String isbn) {
        return isbn.replace("-", "").trim().toUpperCase();
    }

    public String tieBooksToAuthors(String bookId, TieBooksToAuthorsRequest request) {
        BookDocument book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + bookId));

        String normalizedName = normalizeName(request.authorName());

        if (authorRepository.existsByNameIgnoreCase(normalizedName) == false) {
            throw new ResourceNotFoundException("AuthorName not found: " + normalizedName);

        }
        book.setAuthorName(request.authorName().trim());

        bookRepository.save(book);
        return "Book with ID " + bookId + " authorName " + normalizedName;
    }

    private String normalizeName(String name) {
        return name.trim().toLowerCase(Locale.ROOT);
    }

    public BookResponse register(UpdateBookRequest request) {

        String normalizedIsbn = normalizeIsbn(request.isbn());
        String normalizedName = normalizeName(request.authorName());

        if (authorRepository.existsByNameIgnoreCase(normalizedName) == false) {
            throw new ResourceNotFoundException("AuthorName not found: " + normalizedName);
        }

        if (bookRepository.existsByIsbn(normalizedIsbn)) {
            throw new ConflictException("ISBN is already assigned to another book");
        }

        if (request.availableCopies() > request.totalCopies()) {
            throw new BadRequestException("Available copies cannot exceed total copies");
        }

        BookDocument book = new BookDocument();

        book.setTitle(request.title().trim());
        book.setAuthorName(request.authorName().trim());
        book.setIsbn(normalizedIsbn);
        book.setDescription(request.description() == null ? null : request.description().trim());
        book.setPublicationYear(request.publicationYear());
        book.setTotalCopies(request.totalCopies());
        book.setAvailableCopies(request.availableCopies());

        BookDocument saved = bookRepository.save(book);
        return bookMapper.toResponse(saved);

    }

}
