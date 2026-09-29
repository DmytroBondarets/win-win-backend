package com.winwin.authapi.dto;

import jakarta.validation.constraints.NotBlank;

public record ProcessRequest(
        @NotBlank(message = "Text is required")
        String text
) {
}
