package com.examcell.resultgen.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarkEntryDto {

    @NotNull(message = "Student ID cannot be null")
    private UUID studentId;

    @NotNull(message = "Subject ID cannot be null")
    private UUID subjectId;

    @NotNull(message = "Marks Obtained cannot be null")
    @PositiveOrZero(message = "Marks Obtained must be non-negative")
    // TODO: Add validation for marks <= maxMarks (needs access to Subject/config)
    private Double marksObtained;

    // maxMarks could be optionally included if it varies per entry
    // private Double maxMarks = 100.0;
} 