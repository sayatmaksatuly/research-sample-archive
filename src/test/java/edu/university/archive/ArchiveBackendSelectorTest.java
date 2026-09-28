package edu.university.archive;

import edu.university.archive.backend.ArchiveBackendSelector;
import edu.university.archive.backend.CloudArchive;
import edu.university.archive.backend.LegacyArchiveAdapter;
import edu.university.archive.backend.LocalArchive;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArchiveBackendSelectorTest {
    private final ArchiveBackendSelector selector = new ArchiveBackendSelector();

    @Test
    void selectsAllThreeBackendsFromRuntimeText() {
        assertInstanceOf(LocalArchive.class, selector.select("local"));
        assertInstanceOf(CloudArchive.class, selector.select("cloud"));
        assertInstanceOf(LegacyArchiveAdapter.class, selector.select("freezer"));
    }

    @Test
    void rejectsUnsupportedChoice() {
        assertThrows(IllegalArgumentException.class, () -> selector.select("database"));
        assertThrows(IllegalArgumentException.class, () -> selector.select(null));
    }
}
