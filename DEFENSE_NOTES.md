# Oral Defense Notes

1. **What problem does Bridge solve here?** It avoids separate classes for every registration type and archive backend combination.
2. **What exactly is the bridge?** The `ArchiveBackend` reference held by `SampleRegistration`, which lets registration delegate storage.
3. **What is the Abstraction?** `SampleRegistration`.
4. **What is the Implementor?** The `ArchiveBackend` interface.
5. **Why composition instead of inheritance?** A registration can hold any backend without inheriting one backend's behavior or creating combination subclasses.
6. **What are the Refined Abstractions?** `FieldSampleRegistration` and `LabSampleRegistration`.
7. **Why is `OldFreezerRegistry` incompatible?** Its method takes a freezer number, researcher code, and serialized string and returns status codes instead of accepting `SampleRecord` and throwing exceptions.
8. **Why do you need Adapter?** To convert a modern record and failure contract to the old registry and back.
9. **Why is Bridge alone insufficient?** The old registry does not implement `ArchiveBackend`.
10. **Why is Adapter alone insufficient?** It would integrate the old registry but leave registration types coupled to storage choices.
11. **Where are legacy errors translated?** In `LegacyArchiveAdapter.save` after `putIntoFreezer` returns.
12. **Does legacy information leak into registration?** No. Registration classes use only `ArchiveBackend`, `SampleRecord`, and modern exceptions.
13. **How can you add a new backend?** Implement `ArchiveBackend`; field and lab registration need no edits. Add a selector name if the console must expose it.
14. **How can you add a new registration type?** Extend `SampleRegistration`, add its rules, and call `archive`; backend classes need no edits.
15. **How does dynamic backend selection work?** `Main` passes the second runtime argument to `ArchiveBackendSelector.select`.
16. **How is OCP satisfied?** Each Bridge axis can gain a subtype without changing classes on the other axis.
17. **What is one limitation?** The legacy boundary flattens typed data into a pipe-delimited string.
18. **What do the tests prove?** They verify both registration delegates, modern failure propagation, all legacy code translations, selection, and real flow through all three backends.
