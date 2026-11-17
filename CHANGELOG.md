# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/)
and this project adheres to [Semantic Versioning](https://semver.org/).

## [0.1.14](https://github.com/openfga/intellij-plugin/compare/v0.1.13...v0.1.14) (2026-11-17)


### Security

* bump `openfga-language` from 0.2.1 to 0.3.0 ([pkg/java/v0.3.0](https://github.com/openfga/language/releases/tag/pkg%2Fjava%2Fv0.3.0)) to drop its transitive Jackson 2.15.3 dependency, which carried open advisories against `jackson-databind` (including [CVE-2026-54512](https://github.com/advisories/GHSA-j3rv-43j4-c7qm), [CVE-2026-54513](https://github.com/advisories/GHSA-rmj7-2vxq-3g9f), [CVE-2026-68497](https://github.com/advisories/GHSA-q4xh-88c3-wmh7), [CVE-2026-91776](https://github.com/advisories/GHSA-wv8q-qhhj-9h54), [CVE-2026-91777](https://github.com/advisories/GHSA-cxp5-3px4-pw24)) and `jackson-core` (including [CVE-2026-68494](https://github.com/advisories/GHSA-r7wm-3cxj-wff9), [CVE-2026-89425](https://github.com/advisories/GHSA-7hhh-6rmp-j9qf)). The plugin now resolves Jackson 2.22.4 and snakeyaml 2.5. ([#226](https://github.com/openfga/intellij-plugin/pull/226)) ([0267dab](https://github.com/openfga/intellij-plugin/commit/0267dab6453b750735d240fa506d58640cb71fef))
* bump `openfga-sdk` from 0.11.0 to 0.11.2 to drop its transitive OpenTelemetry 1.65.0 dependency, affected by [CVE-2026-118259](https://github.com/advisories/GHSA-ccrj-gw8r-854m). The plugin now resolves OpenTelemetry 1.67.0. ([#224](https://github.com/openfga/intellij-plugin/pull/224)) ([3acfe3d](https://github.com/openfga/intellij-plugin/commit/3acfe3d22116e020a825c0b423a62edbe7a06646))
* with these bumps, no artifact on the plugin's runtime classpath has a known advisory in the OSV or GitHub Advisory databases


### Changed

* `openfga-language` 0.3.0 adds support for the `$expression` reserved condition name on type restrictions and reports correct error locations when type or schema lines have trailing comments

## [0.1.13](https://github.com/openfga/intellij-plugin/compare/v0.1.12...v0.1.13) (2026-11-16)


### Added

* support 2026.3.* based IDEs ([#220](https://github.com/openfga/intellij-plugin/issues/220)) ([fd58924](https://github.com/openfga/intellij-plugin/commit/fd589249e0409eb147b20d65611f577eed36d551))

## [0.1.12](https://github.com/openfga/intellij-plugin/compare/v0.1.11...v0.1.12) (2026-07-28)


### Added

* support multiple tuple files in the store file ([#182](https://github.com/openfga/intellij-plugin/issues/182)) ([9ab3b70](https://github.com/openfga/intellij-plugin/commit/9ab3b708db4375dfac77336ff3aded92e8625800))


### Fixed

* add reference attributes in color settings page ([#190](https://github.com/openfga/intellij-plugin/issues/190)) ([c7fb695](https://github.com/openfga/intellij-plugin/commit/c7fb6950a2c00351ec3589f083ef5e0ba1f587e3))
* let relation references inherit the scheme's identifier color ([#189](https://github.com/openfga/intellij-plugin/issues/189)) ([1377984](https://github.com/openfga/intellij-plugin/commit/1377984a777a0a3008918dcf6f1d8aa225d56c3e))
* **lexer:** treat whitespace-only lines as skippable whitespace ([#163](https://github.com/openfga/intellij-plugin/issues/163)) ([#187](https://github.com/openfga/intellij-plugin/issues/187)) ([8828c12](https://github.com/openfga/intellij-plugin/commit/8828c12185fd93e181efaa867a89349b9fd89375))


## [0.1.11](https://github.com/openfga/intellij-plugin/compare/v0.1.10...v0.1.11) (2026-07-17)


### Added

* support 2026.2.* based IDEs ([d855da1](https://github.com/openfga/intellij-plugin/commit/d855da1b64fc9de5a41d4ca21077951a20847a8b))


## [0.1.10](https://github.com/openfga/intellij-plugin/compare/v0.1.9...v0.1.10) (2026-07-07)

### Changed

* bump `@openfga/language` package to the latest version


## [0.1.9] - 2026-04-05

Added
- add support for 2026.1.* based IDEs

## [0.1.8] - 2026-03-12

Fixed
- removed invasive IDE theme, now using additionalTextAttributes for syntax highlighting ([#143](https://github.com/openfga/intellij-plugin/pull/143))

## [0.1.7] - 2026-01-12

Added
- add support for 2025.3.* based IDE
- add OpenSSF scorecard badge

## [0.1.6] - 2025-08-07

Added
- add support for 2025.2.* based IDEs ([#108](https://github.com/openfga/intellij-plugin/pull/108))

## [0.1.5] - 2025-03-03

Added
- add support for 2025.* based IDEs ([#80](https://github.com/openfga/intellij-plugin/pull/80))

## [0.1.4] - 2024-12-02

Added
- add support for IDEs based on IntelliJ IDEA 2024.3.* (https://github.com/openfga/intellij-plugin/pull/65) - thanks @edlundin

## [0.1.3] - 2024-09-06

Added
- add support for 2024.2.* based IDEs

Fixed
- `tupleuserset-not-direct` is now prioritized above `no-entrypoint` error (inherited from https://github.com/openfga/language/releases/tag/pkg%2Fjava%2Fv0.2.0-beta.2)
- correct based index for reported errors that was causing the wrong location to be highlighted (inherited from https://github.com/openfga/language/releases/tag/pkg%2Fjava%2Fv0.2.0-beta.2)

Removed
- Remove UI theme (we now only offer an editor theme) (#30)

## [0.1.2] - 2024-06-13

- No functional changes

## [0.1.1] - 2024-05-31

- feat: validation for OpenFGA model files
- feat: validation for OpenFGA models in store files

## [0.1.0] - 2024-05-10

Initial Release
- feat: custom OpenFGA dark theme
- feat: DSL syntax support and validation
- feat: authorization model dsl file and live templates
- feat: generate json file from DSL (requires OpenFGA CLI to be installed)
- feat: configure servers in OpenFGA tool window

[Unreleased]: https://github.com/openfga/intellij-plugin/compare/v0.1.9...HEAD
[0.1.9]: https://github.com/openfga/intellij-plugin/compare/v0.1.8...v0.1.9
[0.1.8]: https://github.com/openfga/intellij-plugin/compare/v0.1.7...v0.1.8
[0.1.7]: https://github.com/openfga/intellij-plugin/compare/v0.1.6...v0.1.7
[0.1.6]: https://github.com/openfga/intellij-plugin/compare/v0.1.5...v0.1.6
[0.1.5]: https://github.com/openfga/intellij-plugin/compare/v0.1.4...v0.1.5
[0.1.4]: https://github.com/openfga/intellij-plugin/compare/v0.1.3...v0.1.4
[0.1.3]: https://github.com/openfga/intellij-plugin/compare/v0.1.2...v0.1.3
[0.1.2]: https://github.com/openfga/intellij-plugin/compare/v0.1.1...v0.1.2
[0.1.1]: https://github.com/openfga/intellij-plugin/compare/v0.1.0...v0.1.1
[0.1.0]: https://github.com/openfga/intellij-plugin/releases/tag/v0.1.0
