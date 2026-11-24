package com.library.management;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.library.management.author.repository.AuthorRepository;
import com.library.management.book.dto.TieBooksToAuthorsRequest;
import com.library.management.book.repository.BookRepository;
import com.library.management.book.service.BookMapper;
import com.library.management.book.service.BookService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class TieAuthorRequestTest {
    @Mock

    private BookRepository bookRepository;
    private AuthorRepository authorRepository;

    private BookService bookService;

    private static Validator validator;

    private BookMapper bookMapper;

    @BeforeEach
    void setUp() {
        bookService = new BookService(bookRepository, new BookMapper(), authorRepository);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void TestIfNameIsBlankItShouldRejects() {
        TieBooksToAuthorsRequest request = new TieBooksToAuthorsRequest(
                ""

        );

        Set<ConstraintViolation<TieBooksToAuthorsRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void TestIfNameIsNullItShouldRejects() {
        TieBooksToAuthorsRequest request = new TieBooksToAuthorsRequest(null);
        Set<ConstraintViolation<TieBooksToAuthorsRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void TestIfAuthorNameIsAboveRangeItShouldRejects() {

        String name = "a".repeat(161);
        TieBooksToAuthorsRequest request = new TieBooksToAuthorsRequest(name);

        Set<ConstraintViolation<TieBooksToAuthorsRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

}
