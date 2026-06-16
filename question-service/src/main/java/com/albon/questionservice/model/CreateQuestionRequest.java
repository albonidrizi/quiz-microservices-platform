package com.albon.questionservice.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateQuestionRequest(
        @NotBlank @Size(max = 500) String questionTitle,
        @NotBlank @Size(max = 255) String option1,
        @NotBlank @Size(max = 255) String option2,
        @NotBlank @Size(max = 255) String option3,
        @NotBlank @Size(max = 255) String option4,
        @NotBlank @Size(max = 255) String rightAnswer,
        @NotBlank @Size(max = 50) String difficultylevel,
        @NotBlank @Size(max = 100) String category) {
}
