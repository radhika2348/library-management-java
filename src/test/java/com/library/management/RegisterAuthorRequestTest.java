package com.library.management;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.library.management.author.dto.RegisterAuthorRequest;
import com.library.management.author.repository.AuthorRepository;
import com.library.management.book.repository.BookRepository;

import com.library.management.author.service.AuthorMapper;
import com.library.management.author.service.AuthorService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ExtendWith(MockitoExtension.class)

public class RegisterAuthorRequestTest {

    @Mock
    private AuthorRepository authorRepository;
    private BookRepository bookRepository;


    private AuthorService authorService;

    private static Validator validator;

    private AuthorMapper authorMapper;

    @BeforeEach
    void setUp() {
        authorService = new AuthorService(authorRepository, new AuthorMapper(),bookRepository);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void TestIfNameIsBlankItShouldRejects() {
        RegisterAuthorRequest request = new RegisterAuthorRequest(
                "",
                "Password1",
                2005,
                "Italian");

        Set<ConstraintViolation<RegisterAuthorRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void TestIfNameIsNullItShouldRejects() {
        RegisterAuthorRequest request = new RegisterAuthorRequest(null, "gwegwr", 2001, "Italian");
        Set<ConstraintViolation<RegisterAuthorRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void TestIfAuthorNameIsAboveRangeItShouldRejects() {

        String name = "a".repeat(161);
        RegisterAuthorRequest request = new RegisterAuthorRequest(name, "rhgr", 2003, "Italian");

        Set<ConstraintViolation<RegisterAuthorRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void TestBiographyIsOptionalItAccepts() {

        RegisterAuthorRequest request = new RegisterAuthorRequest(
                "radhika",
                "",
                2005,
                "Italian");

        Set<ConstraintViolation<RegisterAuthorRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());

    }

    @Test
    void TestNationalityIsOptionalItAccepts() {

        RegisterAuthorRequest request = new RegisterAuthorRequest(
                "radhika",
                "hiwehr ehruewr r98r",
                2005,
                "");

        Set<ConstraintViolation<RegisterAuthorRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());

    }

    @Test
    void TestIfBiographyIsAboveRangeItShouldRejects() {

        String biography = "a".repeat(4001);
        RegisterAuthorRequest request = new RegisterAuthorRequest("uygwerewr", biography, 2003, "xyz");

        Set<ConstraintViolation<RegisterAuthorRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void TestIfNationalityIsAboveRangeItShouldRejects() {

        String nationality = "a".repeat(81);
        RegisterAuthorRequest request = new RegisterAuthorRequest("uygwerewr", "hufyrhtjhrtiueritrth", 2003,
                nationality);

        Set<ConstraintViolation<RegisterAuthorRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

}