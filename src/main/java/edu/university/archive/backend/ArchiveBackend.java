package edu.university.archive.backend;

import edu.university.archive.exception.ArchiveException;
import edu.university.archive.model.SampleRecord;

public interface ArchiveBackend {
    void save(SampleRecord sample) throws ArchiveException;
}
