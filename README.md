# OpenFGA plugin for JetBrain IDEs

This is the official IntelliJ plugin for [OpenFGA](https://openfga.dev/).

[![OpenSSF Scorecard](https://api.securityscorecards.dev/projects/github.com/openfga/intellij-plugin/badge)](https://securityscorecards.dev/viewer/?uri=github.com/openfga/intellij-plugin)
[![Join our community](https://img.shields.io/badge/slack-cncf_%23openfga-40abb8.svg?logo=slack)](https://openfga.dev/community)
[![X](https://img.shields.io/twitter/follow/openfga?color=%23179CF0&logo=X&style=flat-square "@openfga on X")](https://x.com/openfga)

## About

[OpenFGA](https://openfga.dev) is an open source Fine-Grained Authorization solution inspired by [Google's Zanzibar paper](https://research.google/pubs/pub48190/). It was created by the FGA team at [Auth0](https://auth0.com) based on [Auth0 Fine-Grained Authorization (FGA)](https://fga.dev), available under [a permissive license (Apache-2)](https://github.com/openfga/rfcs/blob/main/LICENSE) and welcomes community contributions.

OpenFGA is designed to make it easy for application builders to model their permission layer, and to add and integrate fine-grained authorization into their applications. OpenFGA’s design is optimized for reliability and low latency at a high scale.

## Resources

- [OpenFGA Documentation](https://openfga.dev/docs)
- [OpenFGA API Documentation](https://openfga.dev/api/service)
- [OpenFGA on X](https://x.com/openfga)
- [OpenFGA Community](https://openfga.dev/community)
- [Zanzibar Academy](https://zanzibar.academy)
- [Google's Zanzibar Paper (2019)](https://research.google/pubs/pub48190/)


## Installation

### Manual Installation

![manual_install.png](docs/manual_install.png)

## Usage

* IDE and Editor theme `OpenFGA Dark`
![openfga_dark_theme.png](docs/openfga_dark_theme.png)
* DSL syntax support (associated with `.fga` and `.openfga` file extensions)
![syntax.png](docs/syntax.png)
* DSL syntax injection for YAML store files (associated with `.fga.yaml` and `.openfga.yaml` file extensions)
![store_syntax.png](docs/store_syntax.png)
* Authorization model dsl file template
![template.png](docs/template.png)
* Authorization model dsl live templates
![live_templates.png](docs/live_templates.png)
* Generate json file from DSL (requires [OpenFGA CLI](https://github.com/openfga/cli) to be installed)
![cli_setup.png](docs/cli_setup.png)
![transform_json.png](docs/transform_json.png)
* Configure servers in OpenFGA tool window
![server_setup.png](docs/server_setup.png)

## Roadmap

A rough [roadmap](https://github.com/orgs/openfga/projects/1) for development priorities.

## Contributing

See the [DEVELOPMENT](./docs/DEVELOPMENT.md) and [CONTRIBUTING](https://github.com/openfga/.github/blob/main/CONTRIBUTING.md).

## Author

[OpenFGA](https://github.com/openfga)

## Acknowledgments

A special thanks to [Yann D'Isanto](https://github.com/le-yams) for the contribution of [their codebase](https://github.com/le-yams/openfga4intellij), which this project is built upon.

## License

This project is licensed under the Apache-2.0 license. See the [LICENSE](https://github.com/openfga/vscode-ext/blob/main/LICENSE) file for more info.
