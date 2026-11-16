package com.library.management;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.library.management.author.dto.AuthorResponse;
import com.library.management.author.dto.RegisterAuthorRequest;
import com.library.management.author.model.AuthorDocument;
import com.library.management.author.repository.AuthorRepository;
import com.library.management.author.service.AuthorMapper;
import com.library.management.author.service.AuthorService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ExtendWith(MockitoExtension.class)

public class LibraryEntityInputTest {

    @Mock
    private AuthorRepository authorRepository;

    private AuthorService authorService;

    private static Validator validator;

    private AuthorMapper authorMapper;

    @BeforeEach
    void setUp() {
        authorService = new AuthorService(authorRepository, new AuthorMapper());
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
    void rejectsIfBiographyIsAboveRange() {

        String biography = "a".repeat(4001);
        RegisterAuthorRequest request = new RegisterAuthorRequest("uygwerewr", biography, 2003, "xyz");

        Set<ConstraintViolation<RegisterAuthorRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void rejectsIfNationalityIsAboveRange() {

        String nationality = "a".repeat(81);
        RegisterAuthorRequest request = new RegisterAuthorRequest("uygwerewr", "hufyrhtjhrtiueritrth", 2003,
                nationality);

        Set<ConstraintViolation<RegisterAuthorRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void validateFindByNameIgnoreCase() {

        AuthorDocument author = authorRepository.findByNameIgnoreCase("abc").orElse(null);
        String expected = "author not found";
        String actual = "";
        if (author == null) {
            actual = "author not found";
        }
        assertEquals(expected, actual);
    }

    @Test
    void validateStoreAuthorWithNormalizedName() {
        RegisterAuthorRequest request = new RegisterAuthorRequest(
                " Alexa  ",
                "Password1",
                2005,
                "Italian");

        when(authorRepository.existsByNameIgnoreCase("alexa")).thenReturn(false);
        when(authorRepository.save(any(AuthorDocument.class))).thenAnswer(invocation -> {
            AuthorDocument document = invocation.getArgument(0);
            document.setID("author-1");
            return document;

        });

        AuthorResponse response = authorService.register(request);

        ArgumentCaptor<AuthorDocument> captor = ArgumentCaptor.forClass(AuthorDocument.class);
        verify(authorRepository).save(captor.capture());
        AuthorDocument saved = captor.getValue();
        assertThat(saved.getName()).isEqualTo("alexa");

        assertThat(saved.getBiography()).isEqualTo("Password1");
        assertThat(saved.getYearOfBirth()).isEqualTo(2005);
        assertThat(saved.getNationality()).isEqualTo("Italian");

        assertThat(response.name()).isEqualTo("alexa");

    }
}