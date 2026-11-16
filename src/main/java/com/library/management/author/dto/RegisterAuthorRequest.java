package com.library.management.author.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterAuthorRequest(
        @NotNull(message = "Author name is compulsory") @NotBlank(message = "Author name is required") @Size(min = 1, max = 160, message = "Author name must be between 1 and 160 characters") String name,
        @Size(min = 1, max = 4000, message = "Biography must be at most 4000 characters") String biography,
        @Min(value = 1, message = "year must be 1or later") @Max(value = 2026, message = " year must be 2026or earlier") Integer yearOfBirth,
        @Size(min = 1, max = 80, message = "Nationality is atmost 80 characters") String nationality) {
}
