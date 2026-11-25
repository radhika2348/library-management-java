package com.library.management;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.library.management.author.repository.AuthorRepository;
import com.library.management.book.dto.UpdateBookRequest;
import com.library.management.book.repository.BookRepository;
import com.library.management.book.service.BookMapper;
import com.library.management.book.service.BookService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class UpdateBookRequestTest {

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
    void TestIfTitleIsBlankItShouldRejects() {
        UpdateBookRequest request = new UpdateBookRequest(
                "",
                "Author",
                "9780596009205",
                " integration test",
                2019,
                6,
                5);

        Set<ConstraintViolation<UpdateBookRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void TestIfTitleIsNullItShouldRejects() {
        UpdateBookRequest request = new UpdateBookRequest(
                null,
                "Author",
                "9780596009205",
                " integration test",
                2019,
                6,
                5);

        Set<ConstraintViolation<UpdateBookRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void TestIfTitleIsAboveRangeItShouldRejects() {

        String title = "a".repeat(201);
        UpdateBookRequest request = new UpdateBookRequest(title, "Author",
                "9780596009205",
                " integration test",
                2019,
                6,
                5);

        Set<ConstraintViolation<UpdateBookRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void TestIfAuthorNameIsBlankItShouldRejects() {
        UpdateBookRequest request = new UpdateBookRequest(
                "title",
                "",
                "9780596009205",
                " integration test",
                2019,
                6,
                5);

        Set<ConstraintViolation<UpdateBookRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void TestIfAuthorNameIsNullItShouldRejects() {
        UpdateBookRequest request = new UpdateBookRequest(
                "title",
                null,
                "9780596009205",
                " integration test",
                2019,
                6,
                5);
        Set<ConstraintViolation<UpdateBookRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void TestIfAuthorNameIsAboveRangeItShouldRejects() {

        String name = "a".repeat(161);
        UpdateBookRequest request = new UpdateBookRequest(
                "title",
                name,
                "9780596009205",
                " integration test",
                2019,
                6,
                5);
        Set<ConstraintViolation<UpdateBookRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void TestIfISBNIsBlankItShouldRejects() {
        UpdateBookRequest request = new UpdateBookRequest(
                "title",
                "author",
                "",
                " integration test",
                2019,
                6,
                5);

        Set<ConstraintViolation<UpdateBookRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void TestIfISBNIsNullItShouldRejects() {
        UpdateBookRequest request = new UpdateBookRequest(
                "title",
                "author",
                null,
                " integration test",
                2019,
                6,
                5);

        Set<ConstraintViolation<UpdateBookRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void TestIfISBNIsAboveRangeItShouldRejects() {

        String isbn = "a".repeat(161);
        UpdateBookRequest request = new UpdateBookRequest(
                "title",
                "author",
                isbn,
                " integration test",
                2019,
                6,
                5);
        Set<ConstraintViolation<UpdateBookRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void TestIfDescriptionIsBlankItShouldRejects() {
        String description = "a".repeat(2001);
        UpdateBookRequest request = new UpdateBookRequest(
                "title",
                "author",
                "9780596009205",
                description,
                2019,
                6,
                5);

        Set<ConstraintViolation<UpdateBookRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void TestIfTotalCopiesIsNullItShouldRejects() {
        UpdateBookRequest request = new UpdateBookRequest(
                "title",
                "author",
                "9780596009205",
                " integration test",
                2019,
                null,
                5);

        Set<ConstraintViolation<UpdateBookRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void TestIfTotalCopiesIsGreaterThan10000ItShouldRejects() {
        UpdateBookRequest request = new UpdateBookRequest(
                "title",
                "author",
                "9780596009205",
                " integration test",
                2019,
                10001,
                5);

        Set<ConstraintViolation<UpdateBookRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void TestIfAvailableCopiesIsNullItShouldRejects() {
        UpdateBookRequest request = new UpdateBookRequest(
                "title",
                "author",
                "9780596009205",
                " integration test",
                2019,
                6,
                null);

        Set<ConstraintViolation<UpdateBookRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void TestIfAvailableCopiesIsGreaterThan10000ItShouldRejects() {
        UpdateBookRequest request = new UpdateBookRequest(
                "title",
                "author",
                "9780596009205",
                " integration test",
                2019,
                10,
                10001);

        Set<ConstraintViolation<UpdateBookRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

}
