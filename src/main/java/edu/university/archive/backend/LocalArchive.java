package edu.university.archive.backend;

import edu.university.archive.exception.ArchiveFullException;
import edu.university.archive.exception.InvalidSampleException;
import edu.university.archive.model.SampleRecord;

import java.util.LinkedHashMap;
import java.util.Map;

public class LocalArchive implements ArchiveBackend {
    private static final int CAPACITY = 3;
    private final Map<String, SampleRecord> records = new LinkedHashMap<>();

    @Override
    public void save(SampleRecord sample) throws InvalidSampleException, ArchiveFullException {
        if (sample == null || sample.sampleId() == null || sample.sampleId().isBlank()) {
            throw new InvalidSampleException("A sample ID is required.");
        }
        if (records.containsKey(sample.sampleId())) {
            throw new InvalidSampleException("Sample ID already exists in the local archive.");
        }
        if (records.size() >= CAPACITY) {
            throw new ArchiveFullException("The local archive has no free slots.");
        }
        records.put(sample.sampleId(), sample);
    }
}
