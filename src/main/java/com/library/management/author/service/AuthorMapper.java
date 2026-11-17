package com.library.management.author.service;

import org.springframework.stereotype.Component;

import com.library.management.author.dto.AuthorResponse;
import com.library.management.author.dto.AuthorViewResponse;
import com.library.management.author.model.AuthorDocument;

@Component
public class AuthorMapper {

    public AuthorResponse toResponse(AuthorDocument document) {
        return new AuthorResponse(
                document.getId(),
                document.getName(),
                document.getBiography(),
                document.getYearOfBirth(),
                document.getNationality()

        );
    }

    public AuthorViewResponse toAuthorViewResponse(AuthorDocument document) {
        return new AuthorViewResponse(
                document.getName(),
                document.getBiography(),
                document.getYearOfBirth(),
                document.getNationality()

        );
    }
}
