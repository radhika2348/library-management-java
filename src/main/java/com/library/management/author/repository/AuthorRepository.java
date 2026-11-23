package com.library.management.author.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.library.management.author.model.AuthorDocument;

public interface AuthorRepository extends MongoRepository<AuthorDocument, String> {
    Page<AuthorDocument> findByNameContainingIgnoreCaseOrderByName(String name, Pageable pagable);

    boolean existsByNameIgnoreCase(String name);
    boolean existsByIdIgnoreCase(String id);


}
