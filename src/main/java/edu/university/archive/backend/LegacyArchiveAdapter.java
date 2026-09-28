package edu.university.archive.backend;

import edu.university.archive.exception.ArchiveException;
import edu.university.archive.exception.ArchiveFullException;
import edu.university.archive.exception.InvalidSampleException;
import edu.university.archive.exception.UnknownResearcherException;
import edu.university.archive.legacy.OldFreezerRegistry;
import edu.university.archive.model.SampleRecord;

import java.util.Objects;

public class LegacyArchiveAdapter implements ArchiveBackend {
    private final OldFreezerRegistry legacyRegistry;

    public LegacyArchiveAdapter(OldFreezerRegistry legacyRegistry) {
        this.legacyRegistry = Objects.requireNonNull(legacyRegistry);
    }

    @Override
    public void save(SampleRecord sample) throws ArchiveException {
        if (sample == null || sample.sampleId() == null || sample.sampleId().isBlank()
                || sample.researcherId() == null || sample.researcherId().isBlank()
                || sample.description() == null || sample.description().isBlank()
                || sample.collectedAt() == null || sample.sampleType() == null) {
            throw new InvalidSampleException("The freezer archive needs a complete sample record.");
        }
        if (sample.sampleId().contains("|") || sample.description().contains("|")) {
            throw new InvalidSampleException("Sample text cannot contain the legacy separator '|'.");
        }

        if (sample.storageLocation() == null) {
            throw new InvalidSampleException("Use storage location COLD-A or COLD-B for freezer storage.");
        }
        int freezerNumber = switch (sample.storageLocation()) {
            case "COLD-A" -> 1;
            case "COLD-B" -> 2;
            default -> throw new InvalidSampleException("Use storage location COLD-A or COLD-B for freezer storage.");
        };
        String serialized = String.join("|", sample.sampleId(), sample.description(),
                sample.collectedAt().toString(), sample.sampleType().name());
        int result = legacyRegistry.putIntoFreezer(freezerNumber, sample.researcherId(), serialized);
        switch (result) {
            case OldFreezerRegistry.OK -> { return; }
            case OldFreezerRegistry.FREEZER_FULL -> throw new ArchiveFullException("The selected freezer is full.");
            case OldFreezerRegistry.UNKNOWN_RESEARCHER ->
                    throw new UnknownResearcherException("The researcher is not registered with the freezer archive.");
            case OldFreezerRegistry.CORRUPTED_DATA ->
                    throw new InvalidSampleException("The freezer archive rejected the sample data.");
            default -> throw new ArchiveException("The freezer archive returned an unrecognized failure.");
        }
    }
}
