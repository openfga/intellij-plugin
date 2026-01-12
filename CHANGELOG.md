# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/)
and this project adheres to [Semantic Versioning](https://semver.org/).

## [Unreleased]

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

[Unreleased]: https://github.com/openfga/intellij-plugin/compare/v0.1.7...HEAD
[0.1.7]: https://github.com/openfga/intellij-plugin/compare/v0.1.6...v0.1.7
[0.1.6]: https://github.com/openfga/intellij-plugin/compare/v0.1.5...v0.1.6
[0.1.5]: https://github.com/openfga/intellij-plugin/compare/v0.1.4...v0.1.5
[0.1.4]: https://github.com/openfga/intellij-plugin/compare/v0.1.3...v0.1.4
[0.1.3]: https://github.com/openfga/intellij-plugin/compare/v0.1.2...v0.1.3
[0.1.2]: https://github.com/openfga/intellij-plugin/compare/v0.1.1...v0.1.2
[0.1.1]: https://github.com/openfga/intellij-plugin/compare/v0.1.0...v0.1.1
[0.1.0]: https://github.com/openfga/intellij-plugin/releases/tag/v0.1.0
