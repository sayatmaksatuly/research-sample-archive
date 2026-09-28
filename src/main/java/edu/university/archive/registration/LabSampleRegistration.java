package edu.university.archive.registration;

import edu.university.archive.backend.ArchiveBackend;
import edu.university.archive.exception.ArchiveException;
import edu.university.archive.exception.InvalidSampleException;
import edu.university.archive.model.SampleRecord;
import edu.university.archive.model.SampleType;

public class LabSampleRegistration extends SampleRegistration {
    public LabSampleRegistration(ArchiveBackend backend) {
        super(backend);
    }

    @Override
    public void register(SampleRecord sample) throws ArchiveException {
        if (sample == null || sample.sampleType() != SampleType.LAB) {
            throw new InvalidSampleException("Lab registration requires a lab sample.");
        }
        if (sample.description() == null || sample.description().isBlank()) {
            throw new InvalidSampleException("A lab sample needs a description.");
        }
        archive(sample);
    }
}
