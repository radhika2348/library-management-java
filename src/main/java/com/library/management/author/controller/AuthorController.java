package com.library.management.author.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.library.management.author.dto.AuthorResponse;
import com.library.management.author.dto.RegisterAuthorRequest;
import com.library.management.author.service.AuthorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/authors")
@Tag(name = "Authors", description = "Authors registration and account APIs")
@Slf4j
@SecurityRequirement(name = "bearerAuth")

public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new Author", description = "Public endpoint that register AUTHOR account. AuthorName must be unique.")
    @ApiResponse(responseCode = "201", description = "Author registered", content = @Content(schema = @Schema(implementation = AuthorResponse.class)))
    @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content)
    @ApiResponse(responseCode = "409", description = "Author already registered", content = @Content)
    public AuthorResponse register(@Valid @RequestBody RegisterAuthorRequest request) {
        try {
            return authorService.register(request);
        } catch (Exception e) {
            log.error("Failed to register the author", e);
            return null;
        }

    }
}
