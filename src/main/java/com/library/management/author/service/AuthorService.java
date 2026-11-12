package com.library.management.author.service;

import java.util.Locale;

import org.springframework.stereotype.Service;

import com.library.management.author.dto.AuthorResponse;
import com.library.management.author.dto.RegisterAuthorRequest;
import com.library.management.author.model.AuthorDocument;
import com.library.management.author.repository.AuthorRepository;
import com.library.management.exception.ConflictException;
// import java.util.Set;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
        private final AuthorMapper authorMapper;

     public AuthorService(AuthorRepository authorRepository,  AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
       
        this.authorMapper = authorMapper;
    }
    

     private String normalizeName(String name) {
        return name.trim().toLowerCase(Locale.ROOT);
    }

    public AuthorResponse register(RegisterAuthorRequest request){
        String normalizedName = normalizeName(request.name());


        if(authorRepository.existsByNameIgnoreCase(normalizedName)){
            throw new ConflictException("Author already registered");

        }
       

        AuthorDocument author=new AuthorDocument();
        author.setName(normalizedName);
        author.setBiography(request.biography());
        author.setYearOfBirth(request.yearOfBirth());
        author.setNationality(request.nationality());

        AuthorDocument saved=authorRepository.save(author);
        return authorMapper.toResponse(saved);

    }
  

}
