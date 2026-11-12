package com.library.management.author.repository;
import com.library.management.author.model.AuthorDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface AuthorRepository extends MongoRepository<AuthorDocument,String>{
    Optional<AuthorDocument> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}
