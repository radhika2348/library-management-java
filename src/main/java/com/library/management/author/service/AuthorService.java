package com.library.management.author.service;

import java.util.Locale;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.library.management.author.dto.AuthorResponse;
import com.library.management.author.dto.RegisterAuthorRequest;
import com.library.management.author.model.AuthorDocument;
import com.library.management.author.repository.AuthorRepository;
import com.library.management.book.repository.BookRepository;
import com.library.management.exception.ConflictException;
import com.library.management.exception.ResourceNotFoundException;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    private final AuthorMapper authorMapper;

    public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper, BookRepository bookRepository) {
        this.authorRepository = authorRepository;

        this.authorMapper = authorMapper;
        this.bookRepository = bookRepository;

    }

    private String normalizeName(String name) {
        return name.trim().toLowerCase(Locale.ROOT);
    }

    public AuthorResponse register(RegisterAuthorRequest request) {
        String normalizedName = normalizeName(request.name());

        if (authorRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new ConflictException("Author already registered");

        }

        AuthorDocument author = new AuthorDocument();
        author.setName(normalizedName);
        author.setBiography(request.biography());
        author.setYearOfBirth(request.yearOfBirth());
        author.setNationality(request.nationality());

        AuthorDocument saved = authorRepository.save(author);
        return authorMapper.toResponse(saved);

    }

    public AuthorResponse viewAuthorProfile(String id) {

        AuthorDocument author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found: " + id));

        return authorMapper.toResponse(author);

    }

    public Page<AuthorResponse> searchAuthorsByName(String name, Pageable page) {

        return authorRepository.findByNameContainingIgnoreCaseOrderByName(name, page).map(authorMapper::toResponse);

    }

    public String remove(String authorId) {
        AuthorDocument doc = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found: " + authorId));

        String normalizedName = normalizeName(doc.getName());

        if (bookRepository.existsByAuthorNameIgnoreCase(normalizedName)) {
            throw new ConflictException("AuthorName is  assigned ");
        }

        authorRepository.deleteById(authorId);

        return "Author with ID " + authorId + " deleted successfully!";
    }

}