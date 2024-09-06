## v0.1.3

### [0.1.3](https://github.com/openfga/intellij-plugin/compare/v0.1.2...v0.1.3) (2024-09-06)

Added
* add support for 2024.2.* based IDEs

Fixed
- `tupleuserset-not-direct` is now prioritized above `no-entrypoint` error (inherited from https://github.com/openfga/language/releases/tag/pkg%2Fjava%2Fv0.2.0-beta.2)
- correct based index for reported errors that was causing the wrong location to be highlighted (inherited from https://github.com/openfga/language/releases/tag/pkg%2Fjava%2Fv0.2.0-beta.2)

Removed
- Remove UI theme (we now only offer an editor theme) (#30)

## v0.1.2

### [0.1.2](https://github.com/openfga/intellij-plugin/compare/v0.1.1...v0.1.2) (2024-06-13)

* No functional changes

## v0.1.1

### [0.1.1](https://github.com/openfga/intellij-plugin/compare/v0.1.0...v0.1.1) (2024-05-31)

- feat: validation for OpenFGA model files
- feat: validation for OpenFGA models in store files

## v0.1.0

### [0.1.0](https://github.com/openfga/intellij-plugin/releases/tag/v0.1.0) (2024-05-10)

Initial Release

- feat: custom OpenFGA dark theme
- feat: DSL syntax support and validation
- feat: authorization model dsl file and live templates
- feat: generate json file from DSL (requires OpenFGA CLI to be installed)
- feat: configure servers in OpenFGA tool window
