package edu.university.archive.legacy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OldFreezerRegistry {
    public static final int OK = 0;
    public static final int FREEZER_FULL = -1;
    public static final int UNKNOWN_RESEARCHER = -2;
    public static final int CORRUPTED_DATA = -3;

    private static final int FREEZER_CAPACITY = 2;
    private final Set<String> researcherCodes = Set.of("R-101", "R-202");
    private final Map<Integer, List<String>> freezerContents = new HashMap<>();

    public int putIntoFreezer(int freezerNumber, String researcherCode, String serializedSample) {
        if (freezerNumber < 1 || freezerNumber > 2 || serializedSample == null) {
            return CORRUPTED_DATA;
        }
        String[] parts = serializedSample.split("\\|", -1);
        if (parts.length != 4) {
            return CORRUPTED_DATA;
        }
        for (String part : parts) {
            if (part.isBlank()) {
                return CORRUPTED_DATA;
            }
        }
        if (!researcherCodes.contains(researcherCode)) {
            return UNKNOWN_RESEARCHER;
        }
        List<String> contents = freezerContents.computeIfAbsent(freezerNumber, ignored -> new ArrayList<>());
        if (contents.size() >= FREEZER_CAPACITY) {
            return FREEZER_FULL;
        }
        contents.add(serializedSample);
        return OK;
    }
}
