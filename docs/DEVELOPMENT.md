# Development and Contributing to the OpenFGA IntelliJ Plugin

Read the [OpenFGA Contribution Process](https://github.com/openfga/.github/blob/main/CONTRIBUTING.md) and the [OpenFGA Code of Conduct](https://github.com/openfga/.github/blob/main/CODE_OF_CONDUCT.md).

## Getting Started

### Run Build & tests

`./gradlew build`

### Run IDE extension in sandbox

`./gradlew runIde`

## Distribution (Optional)

To generate an installable build of this plugin:

`./gradlew buildPlugin`

The plugin zip will be output to `build/distributions/`. You can then install it from disk by going to **Settings** > **Plugins** > **⚙️** > **Install Plugin from Disk...** and selecting the zip file.
