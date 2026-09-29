package com.winwin.dataapi.dto;

import jakarta.validation.constraints.NotBlank;

public record TransformRequest(@NotBlank(message = "Text is required") String text) {
}
