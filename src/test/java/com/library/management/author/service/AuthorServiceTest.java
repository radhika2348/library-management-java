package com.library.management.author.service;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.library.management.author.dto.AuthorResponse;
import com.library.management.author.dto.RegisterAuthorRequest;
import com.library.management.author.model.AuthorDocument;
import com.library.management.author.repository.AuthorRepository;
import com.library.management.book.repository.BookRepository;

import com.library.management.exception.ConflictException;
import com.library.management.exception.ResourceNotFoundException;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ExtendWith(MockitoExtension.class)

public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    private BookRepository bookRepository;

    private AuthorService authorService;

    private static Validator validator;

    @BeforeEach
    void setUp() {
        authorService = new AuthorService(authorRepository, new AuthorMapper(), bookRepository);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void registerStoreAuthorWithNormalizedName() {
        RegisterAuthorRequest request = new RegisterAuthorRequest(
                "Alexa",
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

    @Test
    void registerRejectsDuplicateName() {

        when(authorRepository.existsByNameIgnoreCase("alexa")).thenReturn(true);

        assertThatThrownBy(() -> authorService.register(
                new RegisterAuthorRequest("alexa", "Password1", 2005, "Italian")))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Author already registered");
    }

    @Test
    void viewAuthorProfile() {
        RegisterAuthorRequest request = new RegisterAuthorRequest(
                "Alexa",
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

        when(authorRepository.findById("author-1")).thenReturn(Optional.of(saved));
        AuthorResponse response1 = authorService.viewAuthorProfile("author-1");

        assertThat(response1.name()).isEqualTo("alexa");
        assertThat(response1.biography()).isEqualTo("Password1");
        assertThat(response1.yearOfBirth()).isEqualTo(2005);
        assertThat(response1.nationality()).isEqualTo("Italian");

    }

    @Test
    void displayFailsWhenAuthorNotFound() {
        when(authorRepository.findById("101")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.viewAuthorProfile("101"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void searchAuthorNameIfMatchesReturnsAuthorDetails() {
        RegisterAuthorRequest request = new RegisterAuthorRequest(
                "Alexa",
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
        Pageable page = PageRequest.of(0, 1);
        when(authorRepository.findByNameContainingIgnoreCaseOrderByName("z", page))
                .thenReturn(new PageImpl<>(List.of(saved)));

        Page<AuthorResponse> a = authorService.searchAuthorsByName("z", page);

        assertThat(a.getNumberOfElements()).isEqualTo(1);
        assertThat(a.getSize()).isEqualTo(1);
        assertThat(a.getTotalPages()).isEqualTo(1);

    }

    @Test
    void searchWithAuthorNameIfNotMatchesReturnEmptyPage() {
        Pageable page = PageRequest.of(0, 1);
        when(authorRepository.findByNameContainingIgnoreCaseOrderByName("z", page)).thenReturn(Page.empty());

        Page<AuthorResponse> a = authorService.searchAuthorsByName("z", page);

        assertThat(a.getNumberOfElements()).isEqualTo(0);
        assertThat(a.getSize()).isEqualTo(0);

    }

    @Test
    void checkAuthorDeleted() {
        RegisterAuthorRequest request = new RegisterAuthorRequest(
                "Alexa",
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
        assertThat(saved.getId()).isEqualTo("author-1");

        assertThat(saved.getBiography()).isEqualTo("Password1");
        assertThat(saved.getYearOfBirth()).isEqualTo(2005);
        assertThat(saved.getNationality()).isEqualTo("Italian");
        assertThat(response.name()).isEqualTo("alexa");

        authorService.remove("author-1");

        assertThat(saved.getName()).isEqualTo(null);
        assertThat(saved.getBiography()).isEqualTo(null);
        assertThat(saved.getNationality()).isEqualTo(null);

    }

    @Test
    void deleteAuthorIfAuthorNotExistReturnsAuthorNotFound() {
        when(authorRepository.findById("101")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.remove("101"))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}