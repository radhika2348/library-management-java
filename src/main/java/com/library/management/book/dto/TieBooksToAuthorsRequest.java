package com.library.management.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TieBooksToAuthorsRequest(

        @NotBlank(message = "Author name is required") @Size(min = 1, max = 160, message = "Author name must be between 1 and 160 characters") String authorName) {

}
