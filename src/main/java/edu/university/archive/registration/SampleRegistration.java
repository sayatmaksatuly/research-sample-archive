package edu.university.archive.registration;

import edu.university.archive.backend.ArchiveBackend;
import edu.university.archive.exception.ArchiveException;
import edu.university.archive.model.SampleRecord;

import java.util.Objects;

public abstract class SampleRegistration {
    private final ArchiveBackend backend;

    protected SampleRegistration(ArchiveBackend backend) {
        this.backend = Objects.requireNonNull(backend);
    }

    public abstract void register(SampleRecord sample) throws ArchiveException;

    protected final void archive(SampleRecord sample) throws ArchiveException {
        backend.save(sample);
    }
}
