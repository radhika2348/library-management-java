package com.library.management.author.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterAuthorRequest(
                @NotBlank(message = "Author name is required") @Size(min = 1, max = 160, message = "Author name must be between 1 and 160 characters") String name,
                @NotBlank(message = "Biography is required") @Size(max = 4000, message = "Biography must be at most 4000 characters") String biography,
                @Min(value = 1450, message = "year must be 1450 or later") @Max(value = 2026, message = " year must be 2100 or earlier") Integer yearOfBirth,
                @NotBlank(message = "Nationality is required") @Size(min = 2, max = 80, message = "Nationality is atmost 80 characters") String nationality) {
}
