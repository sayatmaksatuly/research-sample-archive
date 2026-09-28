package edu.university.archive.backend;

import edu.university.archive.legacy.OldFreezerRegistry;

import java.util.Locale;

public class ArchiveBackendSelector {
    public ArchiveBackend select(String choice) {
        if (choice == null) {
            throw new IllegalArgumentException("Choose local, cloud, or freezer.");
        }
        return switch (choice.toLowerCase(Locale.ROOT)) {
            case "local" -> new LocalArchive();
            case "cloud" -> new CloudArchive();
            case "freezer" -> new LegacyArchiveAdapter(new OldFreezerRegistry());
            default -> throw new IllegalArgumentException("Unknown backend: " + choice + ". Choose local, cloud, or freezer.");
        };
    }
}
