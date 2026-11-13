package com.library.management.author.service;

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

import com.library.management.author.dto.AuthorResponse;
import com.library.management.author.dto.RegisterAuthorRequest;
import com.library.management.author.model.AuthorDocument;
import com.library.management.author.repository.AuthorRepository;
import com.library.management.exception.ConflictException;

@ExtendWith(MockitoExtension.class)

public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        authorService = new AuthorService(authorRepository, new AuthorMapper());
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

}