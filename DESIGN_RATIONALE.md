# Design Rationale

## Problem

The University Research Sample Archive System registers field and laboratory samples. A registration checks rules for its sample type, then stores the record in a selected archive. The same registration may need a local archive, a cloud-style archive, or an older university freezer registry. This project models those alternatives without adding a database or network service.

## Why Bridge

Registration type and storage backend are separate dimensions. Without Bridge, one possible design would create `FieldLocalRegistration`, `FieldCloudRegistration`, `FieldLegacyRegistration`, `LabLocalRegistration`, `LabCloudRegistration`, and `LabLegacyRegistration`. Every new registration type or backend would add more combinations. Here, `SampleRegistration` holds an `ArchiveBackend` reference. `FieldSampleRegistration` and `LabSampleRegistration` enforce their own rules and call the same `archive` method. The backend can change without changing those classes.

## Why Adapter

`OldFreezerRegistry` represents an existing legacy API and is left outside the modern interface. It calls `putIntoFreezer(int, String, String)`, not `save(SampleRecord)`. Its freezer number, researcher code, and serialized sample have different types and structure from the modern record. It reports failure through integer status codes rather than exceptions. `LegacyArchiveAdapter` maps `COLD-A` or `COLD-B` to a freezer number, serializes the record, calls the old method, and converts every known failure code into a specific `ArchiveException` subtype. An unexpected code also becomes an `ArchiveException`. Legacy details stay inside the adapter boundary.

## Why each pattern is needed

Bridge assumes that every concrete backend satisfies `ArchiveBackend`; `OldFreezerRegistry` does not, so Bridge alone cannot use it. Adapter makes the old registry usable, but Adapter alone does not separate registration types from storage choices. Without that separation, the combination classes or direct dependencies would remain.

## Dynamic Implementor Selection

The chosen complexity module is **Dynamic Implementor Selection**. `Main` receives the registration type and backend name as runtime arguments. `ArchiveBackendSelector` translates `local`, `cloud`, or `freezer` into an `ArchiveBackend`. The client then works through that interface. The project does not implement a two-way adapter.

## Open/Closed Principle

A new registration subtype can call `archive` with an existing backend without changing `LocalArchive`, `CloudArchive`, or `LegacyArchiveAdapter`. A new `ArchiveBackend` implementation can be used by the existing field and lab registrations without changing either class. Exposing a new backend name in the console would require adding its name to the selector, but the two Bridge hierarchies remain untouched.

## Limitation

The old registry accepts a pipe-delimited string, so typed sample information is flattened at that boundary. The adapter rejects a pipe in text fields rather than attempting to represent every possible description in the old format.
