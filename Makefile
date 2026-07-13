# Helpers for the plugin change-notes, which are generated at build time from
# CHANGELOG.md (see changeNotesFromChangelog in build.gradle.kts).

GRADLE ?= ./gradlew
PATCHED_XML = $(shell find build -name plugin.xml -path '*patch*' 2>/dev/null | head -n1)

.PHONY: change-notes print-change-notes check-change-notes clean-change-notes

## change-notes: patch plugin.xml so build/ contains the generated notes
change-notes:
	$(GRADLE) patchPluginXml

## print-change-notes: generate, then print the <change-notes> block
print-change-notes: change-notes
	@xml='$(PATCHED_XML)'; \
	if [ -z "$$xml" ]; then \
		echo "could not find a patched plugin.xml under build/" >&2; \
		exit 1; \
	fi; \
	awk '/<change-notes>/{f=1} f{print} /<\/change-notes>/{exit}' "$$xml"

## check-change-notes: fail if the generated notes are empty or the fallback
check-change-notes: change-notes
	@xml='$(PATCHED_XML)'; \
	if [ -z "$$xml" ]; then \
		echo "could not find a patched plugin.xml under build/" >&2; \
		exit 1; \
	fi; \
	notes=$$(awk '/<change-notes>/{f=1;next} /<\/change-notes>/{exit} f' "$$xml"); \
	if [ -z "$$(echo "$$notes" | tr -d '[:space:]')" ]; then \
		echo "change-notes are empty" >&2; \
		exit 1; \
	fi; \
	if echo "$$notes" | grep -q 'See the CHANGELOG for release notes.'; then \
		version=$$($(GRADLE) properties -q 2>/dev/null | awk -F': ' '/^version:/ {print $$2}'); \
		echo "no CHANGELOG.md section found for version $$version; change-notes fell back to a placeholder" >&2; \
		exit 1; \
	fi; \
	echo "change-notes OK"

## clean-change-notes: remove build output so the next run regenerates cleanly
clean-change-notes:
	$(GRADLE) clean
