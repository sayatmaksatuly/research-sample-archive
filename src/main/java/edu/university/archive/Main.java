package edu.university.archive;

import edu.university.archive.backend.ArchiveBackend;
import edu.university.archive.backend.ArchiveBackendSelector;
import edu.university.archive.exception.ArchiveException;
import edu.university.archive.model.SampleRecord;
import edu.university.archive.model.SampleType;
import edu.university.archive.registration.FieldSampleRegistration;
import edu.university.archive.registration.LabSampleRegistration;
import edu.university.archive.registration.SampleRegistration;

import java.time.LocalDateTime;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java ... Main <field|lab> <local|cloud|freezer>");
            return;
        }

        try {
            ArchiveBackend backend = new ArchiveBackendSelector().select(args[1]);
            String kind = args[0].toLowerCase(Locale.ROOT);
            SampleType sampleType = switch (kind) {
                case "field" -> SampleType.FIELD;
                case "lab" -> SampleType.LAB;
                default -> throw new IllegalArgumentException("Choose field or lab registration.");
            };
            SampleRecord sample = new SampleRecord("S-001", "R-101", "Water quality sample",
                    "COLD-A", LocalDateTime.now().minusDays(1), sampleType);
            SampleRegistration registration = sampleType == SampleType.FIELD
                    ? new FieldSampleRegistration(backend)
                    : new LabSampleRegistration(backend);
            registration.register(sample);
            System.out.println("Registered " + sample.sampleId() + " using " + kind + " registration in " + args[1] + ".");
        } catch (ArchiveException | IllegalArgumentException error) {
            System.err.println("Registration failed: " + error.getMessage());
        }
    }
}
