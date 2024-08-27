# Release Process

## Bump version number

- Update the `version` number in `build.gradle.kts`
- Update the `pluginVersion` number in `gradle.properties`

## Update the CHANGELOG

You will need to;
- update the [CHANGELOG.md](/CHANGELOG.md)
- update the "change-notes" section of the [plugin.xml](/src/main/resources/META-INF/plugin.xml)
    - This will appear in the "What's new?" section of the plugin page

## When ready, push a new tag

`git tag -a -s v[0-9]+.[0-9]+.[0-9]+`