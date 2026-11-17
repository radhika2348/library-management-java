package com.library.management.author.dto;

public record AuthorViewResponse(
                String name,
                String biography,
                Integer yearOfBirth,
                String nationality) {
}
