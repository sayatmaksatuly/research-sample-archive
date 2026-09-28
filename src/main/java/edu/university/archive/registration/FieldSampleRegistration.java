package edu.university.archive.registration;

import edu.university.archive.backend.ArchiveBackend;
import edu.university.archive.exception.ArchiveException;
import edu.university.archive.exception.InvalidSampleException;
import edu.university.archive.model.SampleRecord;
import edu.university.archive.model.SampleType;

import java.time.LocalDateTime;

public class FieldSampleRegistration extends SampleRegistration {
    public FieldSampleRegistration(ArchiveBackend backend) {
        super(backend);
    }

    @Override
    public void register(SampleRecord sample) throws ArchiveException {
        if (sample == null || sample.sampleType() != SampleType.FIELD) {
            throw new InvalidSampleException("Field registration requires a field sample.");
        }
        if (sample.collectedAt() == null || sample.collectedAt().isAfter(LocalDateTime.now())) {
            throw new InvalidSampleException("A field sample needs a collection time that is not in the future.");
        }
        archive(sample);
    }
}
