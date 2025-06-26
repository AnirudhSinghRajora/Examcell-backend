package com.examcell.resultgen.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarkEntryDto {

    @NotNull(message = "Student ID cannot be null")
    private UUID studentId;

    @NotNull(message = "Subject ID cannot be null")
    private UUID subjectId;

    @NotNull(message = "Internal 1 marks cannot be null")
    @PositiveOrZero(message = "Internal 1 marks must be non-negative")
    private Double internal1;

    @NotNull(message = "Internal 2 marks cannot be null")
    @PositiveOrZero(message = "Internal 2 marks must be non-negative")
    private Double internal2;

    @NotNull(message = "External marks cannot be null")
    @PositiveOrZero(message = "External marks must be non-negative")
    private Double external;

    // maxMarks could be optionally included if it varies per entry
    // private Double maxMarks = 100.0;
} 