package com.library.management.author.service;

import com.library.management.author.dto.AuthorResponse;
import com.library.management.author.model.AuthorDocument;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {
    

    public AuthorResponse toResponse(AuthorDocument document){
        return new AuthorResponse(
            document.getID(),
            document.getName(),
            document.getBiography(),
            document.getYearOfBirth(),
            document.getNationality()

        );
    }
}
