package com.library.management.author.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.library.management.author.model.AuthorDocument;

public interface AuthorRepository extends MongoRepository<AuthorDocument, String> {
    Optional<AuthorDocument> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}
