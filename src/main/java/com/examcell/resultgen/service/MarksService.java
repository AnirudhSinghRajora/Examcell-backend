package com.examcell.resultgen.service;

import java.util.List;
import java.util.UUID;

import com.examcell.resultgen.dto.MarkEntryDto;
import com.examcell.resultgen.model.MarksRecord;

public interface MarksService {

    /**
     * Enters marks for a batch of students for specific subjects.
     * Authorization based on professor is removed.
     *
     * @param professorEmployeeId The Employee ID of the professor entering the marks (for tracking).
     * @param markEntries A list of MarkEntryDto objects.
     * @return The list of created or updated MarksRecord entities.
     */
    List<MarksRecord> batchEnterMarks(String professorEmployeeId, List<MarkEntryDto> markEntries);

    /**
     * Updates an existing marks record.
     * Authorization based on professor is removed.
     *
     * @param professorEmployeeId The Employee ID of the professor updating the mark (for tracking).
     * @param recordId The ID of the MarksRecord to update.
     * @param marksObtained The new marks value.
     * @return The updated MarksRecord entity.
     */
    MarksRecord updateMark(String professorEmployeeId, UUID recordId, Double marksObtained);
} 