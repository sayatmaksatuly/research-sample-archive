package edu.university.archive;

import edu.university.archive.backend.ArchiveBackend;
import edu.university.archive.backend.ArchiveBackendSelector;
import edu.university.archive.exception.ArchiveFullException;
import edu.university.archive.exception.UnknownResearcherException;
import edu.university.archive.model.SampleRecord;
import edu.university.archive.model.SampleType;
import edu.university.archive.registration.FieldSampleRegistration;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArchiveIntegrationTest {
    @Test
    void fieldRegistrationWorksWithEverySelectedBackend() {
        ArchiveBackendSelector selector = new ArchiveBackendSelector();
        for (String choice : new String[]{"local", "cloud", "freezer"}) {
            ArchiveBackend backend = selector.select(choice);
            assertDoesNotThrow(() -> new FieldSampleRegistration(backend).register(sample("S-1", "R-101")));
        }
    }

    @Test
    void realLegacyRegistryReportsUnknownResearcherThroughModernContract() {
        ArchiveBackend backend = new ArchiveBackendSelector().select("freezer");

        assertThrows(UnknownResearcherException.class,
                () -> new FieldSampleRegistration(backend).register(sample("S-1", "R-999")));
    }

    @Test
    void realLegacyRegistryReportsCapacityThroughModernContract() throws Exception {
        ArchiveBackend backend = new ArchiveBackendSelector().select("freezer");
        FieldSampleRegistration registration = new FieldSampleRegistration(backend);
        registration.register(sample("S-1", "R-101"));
        registration.register(sample("S-2", "R-101"));

        assertThrows(ArchiveFullException.class, () -> registration.register(sample("S-3", "R-101")));
    }

    private SampleRecord sample(String id, String researcher) {
        return new SampleRecord(id, researcher, "Water sample", "COLD-A",
                LocalDateTime.of(2025, 5, 10, 9, 0), SampleType.FIELD);
    }
}
