package edu.university.archive.model;

import java.time.LocalDateTime;

public record SampleRecord(
        String sampleId,
        String researcherId,
        String description,
        String storageLocation,
        LocalDateTime collectedAt,
        SampleType sampleType) {
}
