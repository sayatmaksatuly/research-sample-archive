package edu.university.archive;

import edu.university.archive.backend.LegacyArchiveAdapter;
import edu.university.archive.exception.ArchiveException;
import edu.university.archive.exception.ArchiveFullException;
import edu.university.archive.exception.InvalidSampleException;
import edu.university.archive.exception.UnknownResearcherException;
import edu.university.archive.legacy.OldFreezerRegistry;
import edu.university.archive.model.SampleRecord;
import edu.university.archive.model.SampleType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class LegacyArchiveAdapterTest {
    @Test
    void convertsTheRecordAndAcceptsLegacySuccess() {
        OldFreezerRegistry legacy = mock(OldFreezerRegistry.class);
        SampleRecord sample = sample("COLD-A");
        String serialized = "S-1|Water sample|2025-05-10T09:00|FIELD";
        when(legacy.putIntoFreezer(1, "R-101", serialized)).thenReturn(OldFreezerRegistry.OK);

        assertDoesNotThrow(() -> new LegacyArchiveAdapter(legacy).save(sample));

        verify(legacy).putIntoFreezer(1, "R-101", serialized);
    }

    @ParameterizedTest
    @MethodSource("legacyFailures")
    void translatesEveryKnownLegacyFailure(int status, Class<? extends ArchiveException> expected) {
        OldFreezerRegistry legacy = mock(OldFreezerRegistry.class);
        when(legacy.putIntoFreezer(eq(2), eq("R-101"), anyString())).thenReturn(status);

        assertThrows(expected, () -> new LegacyArchiveAdapter(legacy).save(sample("COLD-B")));
    }

    static Stream<Arguments> legacyFailures() {
        return Stream.of(
                Arguments.of(OldFreezerRegistry.FREEZER_FULL, ArchiveFullException.class),
                Arguments.of(OldFreezerRegistry.UNKNOWN_RESEARCHER, UnknownResearcherException.class),
                Arguments.of(OldFreezerRegistry.CORRUPTED_DATA, InvalidSampleException.class));
    }

    @Test
    void unknownLegacyStatusStillBecomesAModernException() {
        OldFreezerRegistry legacy = mock(OldFreezerRegistry.class);
        when(legacy.putIntoFreezer(eq(1), eq("R-101"), anyString())).thenReturn(-99);

        ArchiveException error = assertThrows(ArchiveException.class,
                () -> new LegacyArchiveAdapter(legacy).save(sample("COLD-A")));

        assertTrue(error.getMessage().contains("unrecognized"));
    }

    @Test
    void invalidModernLocationNeverReachesTheLegacyRegistry() {
        OldFreezerRegistry legacy = mock(OldFreezerRegistry.class);

        assertThrows(InvalidSampleException.class,
                () -> new LegacyArchiveAdapter(legacy).save(sample("ROOM-7")));

        verifyNoInteractions(legacy);
    }

    private SampleRecord sample(String location) {
        return new SampleRecord("S-1", "R-101", "Water sample", location,
                LocalDateTime.of(2025, 5, 10, 9, 0), SampleType.FIELD);
    }
}
