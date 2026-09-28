package edu.university.archive;

import edu.university.archive.backend.ArchiveBackend;
import edu.university.archive.exception.ArchiveFullException;
import edu.university.archive.exception.InvalidSampleException;
import edu.university.archive.model.SampleRecord;
import edu.university.archive.model.SampleType;
import edu.university.archive.registration.FieldSampleRegistration;
import edu.university.archive.registration.LabSampleRegistration;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class RegistrationTest {
    @Test
    void fieldRegistrationPassesTheOriginalRecordToItsBackend() throws Exception {
        ArchiveBackend backend = mock(ArchiveBackend.class);
        SampleRecord sample = sample(SampleType.FIELD);

        new FieldSampleRegistration(backend).register(sample);

        verify(backend).save(sample);
    }

    @Test
    void labRegistrationPassesTheOriginalRecordToItsBackend() throws Exception {
        ArchiveBackend backend = mock(ArchiveBackend.class);
        SampleRecord sample = sample(SampleType.LAB);

        new LabSampleRegistration(backend).register(sample);

        verify(backend).save(sample);
    }

    @Test
    void wrongRegistrationTypeIsRejectedBeforeCallingTheBackend() {
        ArchiveBackend backend = mock(ArchiveBackend.class);

        assertThrows(InvalidSampleException.class,
                () -> new FieldSampleRegistration(backend).register(sample(SampleType.LAB)));

        verifyNoInteractions(backend);
    }

    @Test
    void backendFailureReachesTheCallerThroughTheModernContract() throws Exception {
        ArchiveBackend backend = mock(ArchiveBackend.class);
        SampleRecord sample = sample(SampleType.LAB);
        doThrow(new ArchiveFullException("No space")).when(backend).save(sample);

        assertThrows(ArchiveFullException.class,
                () -> new LabSampleRegistration(backend).register(sample));
    }

    private SampleRecord sample(SampleType type) {
        return new SampleRecord("S-1", "R-101", "Water sample", "COLD-A",
                LocalDateTime.of(2025, 5, 10, 9, 0), type);
    }
}
