package com.library.management.author.dto;


public record AuthorResponse (
    String id,
    String name,
    String biography,
    Integer yearOfBirth,
    String nationality
){
    
}

    

