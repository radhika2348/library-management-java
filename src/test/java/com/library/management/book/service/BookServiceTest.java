package com.library.management.book.service;

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

import com.library.management.author.repository.AuthorRepository;
import com.library.management.book.dto.BookResponse;
import com.library.management.book.dto.TieBooksToAuthorsRequest;
import com.library.management.book.dto.UpdateBookRequest;
import com.library.management.book.model.BookDocument;
import com.library.management.book.repository.BookRepository;
import com.library.management.exception.BadRequestException;
import com.library.management.exception.ConflictException;
import com.library.management.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;

    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookService(bookRepository, new BookMapper(), authorRepository);
    }

    @Test
    void updateReplacesMutableFields() {
        BookDocument existing = new BookDocument();
        existing.setId("book-1");
        existing.setTitle("Old Title");
        existing.setAuthorName("Old Author");
        existing.setIsbn("9780134685991");
        existing.setTotalCopies(2);
        existing.setAvailableCopies(1);

        UpdateBookRequest request = new UpdateBookRequest(
                "New Title",
                "New Author",
                "9780596009205",
                "Updated description",
                2020,
                5,
                4);

        when(bookRepository.findById("book-1")).thenReturn(Optional.of(existing));
        when(bookRepository.existsByIsbnAndIdNot("9780596009205",
                "book-1")).thenReturn(false);
        when(bookRepository.save(any(BookDocument.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookResponse response = bookService.update("book-1", request);

        assertThat(response.title()).isEqualTo("New Title");
        assertThat(response.authorName()).isEqualTo("New Author");
        assertThat(response.isbn()).isEqualTo("9780596009205");
        assertThat(response.totalCopies()).isEqualTo(5);
        assertThat(response.availableCopies()).isEqualTo(4);
    }

    @Test
    void updateFailsWhenBookMissing() {
        when(bookRepository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.update("missing", sampleRequest()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateFailsWhenIsbnConflicts() {
        BookDocument existing = new BookDocument();
        existing.setId("book-1");
        existing.setIsbn("9780134685991");

        when(bookRepository.findById("book-1")).thenReturn(Optional.of(existing));
        when(bookRepository.existsByIsbnAndIdNot("9780596009205", "book-1")).thenReturn(true);

        assertThatThrownBy(() -> bookService.update("book-1", sampleRequest()))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("ISBN");
    }

    @Test
    void updateFailsWhenAvailableExceedsTotal() {
        BookDocument existing = new BookDocument();
        existing.setId("book-1");
        existing.setIsbn("9780134685991");

        UpdateBookRequest request = new UpdateBookRequest(
                "Title",
                "Author",
                "9780596009205",
                "Desc",
                2020,
                2,
                3);

        when(bookRepository.findById("book-1")).thenReturn(Optional.of(existing));
        when(bookRepository.existsByIsbnAndIdNot("9780596009205", "book-1")).thenReturn(false);

        assertThatThrownBy(() -> bookService.update("book-1", request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Available copies");
    }

    private UpdateBookRequest sampleRequest() {
        return new UpdateBookRequest(
                "Title",
                "Author",
                "9780596009205",
                "Desc",
                2020,
                5,
                4);
    }

    private TieBooksToAuthorsRequest samplRequest() {
        return new TieBooksToAuthorsRequest(
                "author");
    }

    @Test
    void addingAuthorNameToBookFailsWhenBookMissing() {
        when(bookRepository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.update("missing", sampleRequest()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void addingAuthorNameToBookFailsWhenAuthorNameNotExistInCatalog() {
        BookDocument existing = new BookDocument();
        existing.setId("book-1");
        when(bookRepository.findById("book-1")).thenReturn(Optional.of(existing));

        when(authorRepository.existsByNameIgnoreCase("author")).thenReturn(false);

        assertThatThrownBy(() -> bookService.tieBooksToAuthors("book-1",
                samplRequest()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("AuthorName not found");
    }

    @Test
    void testTieBooksToAuthors() {

        UpdateBookRequest request = new UpdateBookRequest(
                "Title",
                "Alexa",
                "9780596009205",
                "Desc",
                2020,
                5,
                4);
        when(bookRepository.save(any(BookDocument.class))).thenAnswer(invocation -> {
            BookDocument document = invocation.getArgument(0);
            document.setId("book-1");
            return document;

        });

        BookResponse response = bookService.register(request);

        ArgumentCaptor<BookDocument> captor = ArgumentCaptor.forClass(BookDocument.class);
        verify(bookRepository).save(captor.capture());
        BookDocument saved = captor.getValue();
        when(bookRepository.findById("book-1")).thenReturn(Optional.of(saved));
        when(authorRepository.existsByNameIgnoreCase("alexar")).thenReturn(true);

        assertThat(response.authorName()).isEqualTo("Alexa");
        assertThat(response.id()).isEqualTo("book-1");

        assertThat(saved.getId()).isEqualTo("book-1");

        TieBooksToAuthorsRequest r = new TieBooksToAuthorsRequest("AlexaR");
        bookService.tieBooksToAuthors("book-1", r);

        assertThat(saved.getAuthorName()).isEqualTo("AlexaR");

    }

}
