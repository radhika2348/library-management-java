package com.library.management.author.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.library.management.author.model.AuthorDocument;



public interface AuthorRepository extends MongoRepository<AuthorDocument, String> {
    List<AuthorDocument> findByNameContainingIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
    
    
}


