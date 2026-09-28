# University Research Sample Archive System

A small Java 17 console project for registering university research samples in one of three archive backends. It combines Bridge and Adapter in the same registration flow. The archives are in-memory demonstrations, so each application run starts with empty storage; no network service or database is required.

## Patterns and architecture

`SampleRegistration` is the Bridge abstraction. `FieldSampleRegistration` and `LabSampleRegistration` apply their own registration rules, then delegate through the `ArchiveBackend` interface. `LocalArchive` and `CloudArchive` implement that interface directly. `LegacyArchiveAdapter` also implements it, but converts a `SampleRecord` into the incompatible `OldFreezerRegistry` call and translates legacy status codes into `ArchiveException` subclasses. `ArchiveBackendSelector` chooses one of the three backends from a runtime argument.

## Requirements and commands

- Java Development Kit 17 or later
- Maven 3.8 or later

From the project root:

```sh
mvn clean test
java -cp target/classes edu.university.archive.Main field local
java -cp target/classes edu.university.archive.Main lab cloud
java -cp target/classes edu.university.archive.Main field freezer
```

The first command compiles the application and runs its JUnit 5 tests. The two application arguments are the registration type (`field` or `lab`) and backend (`local`, `cloud`, or `freezer`). The demonstration record uses researcher `R-101` and storage location `COLD-A`, which are accepted by the legacy registry.

## Project files

- `src/main/java/edu/university/archive/`: console entry point, model, registration hierarchy, backends, adapter, legacy registry, and exceptions.
- `src/test/java/edu/university/archive/`: delegation, error translation, runtime selection, and integration tests.
- `docs/uml.puml`, `docs/uml.svg`, and `docs/uml.png`: source and rendered UML class diagram.
- `DESIGN_RATIONALE.md`: brief explanation of both patterns and the chosen complexity module.
- `DEFENSE_NOTES.md`: short answers for oral preparation.
