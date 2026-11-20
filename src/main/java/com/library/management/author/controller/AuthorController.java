package com.library.management.author.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@RequestMapping("/api/v1")
@Tag(name = "Authors", description = "Authors registration and account APIs")
@Slf4j
@SecurityRequirement(name = "bearerAuth")

public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping(value = "/authors", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new Author", description = "Public endpoint that register AUTHOR account. AuthorName must be unique.")
    @ApiResponse(responseCode = "201", description = "Author registered", content = @Content(schema = @Schema(implementation = AuthorResponse.class)))
    @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content)
    @ApiResponse(responseCode = "409", description = "Author already registered", content = @Content)
    public AuthorResponse register(@Valid @RequestBody RegisterAuthorRequest request) {

        return authorService.register(request);

    }

    @GetMapping(value = "/authors/{id}", produces = MediaType.APPLICATION_JSON_VALUE)

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN','MEMBER')")
    @Operation(summary = "Display Author profile", description = "Public endpoint that display AUTHOR account. AuthorName ID is used to fetch details.")
    @ApiResponse(responseCode = "200", description = "Author Found and Displayed", content = @Content(schema = @Schema(implementation = AuthorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Author was not found", content = @Content)
    @ApiResponse(responseCode = "403", description = "Unauthorized Access", content = @Content)
    public AuthorResponse viewAuthor(@PathVariable String id) {

        return authorService.viewAuthorProfile(id);
    }

    @GetMapping(value = "/authors", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN','MEMBER')")
    @Operation(summary = "Display Author profile", description = "Public endpoint that display AUTHOR account. AuthorName ID is used to fetch details.")
    @ApiResponse(responseCode = "200", description = "Author Found and Displayed", content = @Content(schema = @Schema(implementation = AuthorResponse.class)))
    @ApiResponse(responseCode = "403", description = "Unauthorized Access", content = @Content)

    public Page<AuthorResponse> searchAuthorByName(@RequestParam("name") String name,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return authorService.searchAuthorsByName(name, pageable);
    }

}
