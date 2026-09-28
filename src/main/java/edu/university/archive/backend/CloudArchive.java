package edu.university.archive.backend;

import edu.university.archive.exception.InvalidSampleException;
import edu.university.archive.model.SampleRecord;

import java.util.HashMap;
import java.util.Map;

public class CloudArchive implements ArchiveBackend {
    private final Map<String, Map<String, SampleRecord>> recordsByResearcher = new HashMap<>();

    @Override
    public void save(SampleRecord sample) throws InvalidSampleException {
        if (sample == null || sample.sampleId() == null || sample.sampleId().isBlank()
                || sample.researcherId() == null || sample.researcherId().isBlank()) {
            throw new InvalidSampleException("A sample and researcher ID are required.");
        }
        Map<String, SampleRecord> researcherRecords =
                recordsByResearcher.computeIfAbsent(sample.researcherId(), ignored -> new HashMap<>());
        if (researcherRecords.putIfAbsent(sample.sampleId(), sample) != null) {
            throw new InvalidSampleException("Sample ID already exists for this researcher.");
        }
    }
}
