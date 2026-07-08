package dev.openfga.intellijplugin.language;

import com.intellij.testFramework.fixtures.BasePlatformTestCase;

public class OpenFGATupleFileAnnotatorTest extends BasePlatformTestCase {

    private static final String EXTENSION_ERROR =
            "Tuple file must have one of the following extensions: .json, .jsonl, .yaml, .yml, .csv";

    private void checkStoreFileHighlighting(String text) {
        myFixture.configureByText("store.fga.yaml", text);
        myFixture.checkHighlighting();
    }

    // The tuple fields are optional: no error when none of tuples, tuple_file or tuple_files is set

    public void testStoreFileWithoutAnyTupleFieldsIsValid() {
        checkStoreFileHighlighting("""
                name: Test store
                tests:
                  - name: test-1
                """);
    }

    public void testGlobalInlineTuplesAreValid() {
        checkStoreFileHighlighting(
                """
                name: Test store
                tuples:
                  - user: user:anne
                    relation: viewer
                    object: document:1
                """);
    }

    public void testEmptyTupleFileValueIsValid() {
        checkStoreFileHighlighting("""
                name: Test store
                tuple_file:
                """);
    }

    public void testEmptyTupleFilesValueIsValid() {
        checkStoreFileHighlighting("""
                name: Test store
                tuple_files:
                """);
    }

    // Supported extensions: json, jsonl, yaml, yml and csv

    public void testTupleFileWithSupportedExtensionIsValid() {
        myFixture.addFileToProject("tuples.yaml", "");

        checkStoreFileHighlighting("""
                name: Test store
                tuple_file: ./tuples.yaml
                """);
    }

    public void testTupleFileWithJsonlExtensionIsValid() {
        myFixture.addFileToProject("tuples.jsonl", "");

        checkStoreFileHighlighting("""
                name: Test store
                tuple_file: ./tuples.jsonl
                """);
    }

    public void testTupleFilesWithAllSupportedExtensionsAreValid() {
        myFixture.addFileToProject("tuples.json", "");
        myFixture.addFileToProject("tuples.jsonl", "");
        myFixture.addFileToProject("tuples.yaml", "");
        myFixture.addFileToProject("tuples.yml", "");
        myFixture.addFileToProject("tuples.csv", "");

        checkStoreFileHighlighting(
                """
                name: Test store
                tuple_files:
                  - ./tuples.json
                  - ./tuples.jsonl
                  - ./tuples.yaml
                  - ./tuples.yml
                  - ./tuples.csv
                """);
    }

    public void testTupleFileExtensionIsCaseInsensitive() {
        myFixture.addFileToProject("tuples.JSON", "");

        checkStoreFileHighlighting("""
                name: Test store
                tuple_file: ./tuples.JSON
                """);
    }

    public void testTupleFileWithUnsupportedExtensionIsAnError() {
        checkStoreFileHighlighting(
                """
                name: Test store
                tuple_file: <error descr="%s">./tuples.txt</error>
                """
                        .formatted(EXTENSION_ERROR));
    }

    public void testTupleFileWithoutExtensionIsAnError() {
        checkStoreFileHighlighting(
                """
                name: Test store
                tuple_file: <error descr="%s">./tuples</error>
                """
                        .formatted(EXTENSION_ERROR));
    }

    public void testTupleFilesEntryWithUnsupportedExtensionIsAnError() {
        myFixture.addFileToProject("tuples.json", "");

        checkStoreFileHighlighting(
                """
                name: Test store
                tuple_files:
                  - ./tuples.json
                  - <error descr="%s">./tuples.txt</error>
                """
                        .formatted(EXTENSION_ERROR));
    }

    // Referenced files must exist relative to the store file

    public void testMissingTupleFileIsAnError() {
        checkStoreFileHighlighting(
                """
                name: Test store
                tuple_file: <error descr="Tuple file not found: ./missing.json">./missing.json</error>
                """);
    }

    public void testMissingTupleFilesEntryIsAnError() {
        myFixture.addFileToProject("tuples.json", "");

        checkStoreFileHighlighting(
                """
                name: Test store
                tuple_files:
                  - ./tuples.json
                  - <error descr="Tuple file not found: ./missing.jsonl">./missing.jsonl</error>
                """);
    }

    public void testTupleFileInSubdirectoryIsResolved() {
        myFixture.addFileToProject("sub/tuples.yaml", "");

        checkStoreFileHighlighting("""
                name: Test store
                tuple_file: ./sub/tuples.yaml
                """);
    }

    // Structural validation

    public void testTupleFileMustBeASingleFilePath() {
        checkStoreFileHighlighting(
                """
                name: Test store
                tuple_file:
                  <error descr="tuple_file must be a single file path">- ./tuples.json</error>
                """);
    }

    public void testTupleFilesMustBeAListOfFilePaths() {
        checkStoreFileHighlighting(
                """
                name: Test store
                tuple_files: <error descr="tuple_files must be a list of file paths">./tuples.json</error>
                """);
    }

    // Test scope: the same fields are supported and validated within tests

    public void testTestScopeTupleFileAndTupleFilesAreValid() {
        myFixture.addFileToProject("tuples.json", "");
        myFixture.addFileToProject("more_tuples.jsonl", "");

        checkStoreFileHighlighting(
                """
                name: Test store
                tests:
                  - name: test-1
                    tuple_file: ./tuples.json
                  - name: test-2
                    tuple_files:
                      - ./tuples.json
                      - ./more_tuples.jsonl
                """);
    }

    public void testTestScopeTupleFileWithUnsupportedExtensionIsAnError() {
        checkStoreFileHighlighting(
                """
                name: Test store
                tests:
                  - name: test-1
                    tuple_file: <error descr="%s">./tuples.exe</error>
                """
                        .formatted(EXTENSION_ERROR));
    }

    // tuples, tuple_file and tuple_files can be used together, globally and per test

    public void testCombinedTuplesTupleFileAndTupleFilesAreValid() {
        myFixture.addFileToProject("tuples.json", "");
        myFixture.addFileToProject("tuples_2.yaml", "");
        myFixture.addFileToProject("tuples_3.csv", "");

        checkStoreFileHighlighting(
                """
                name: Test store
                tuples:
                  - user: user:anne
                    relation: viewer
                    object: document:1
                tuple_file: ./tuples.json
                tuple_files:
                  - ./tuples_2.yaml
                  - ./tuples_3.csv
                tests:
                  - name: test-1
                    tuples:
                      - user: user:bob
                        relation: viewer
                        object: document:2
                    tuple_file: ./tuples.json
                    tuple_files:
                      - ./tuples_2.yaml
                """);
    }

    // Files that are not store files are not validated

    public void testNonStoreYamlFilesAreNotValidated() {
        myFixture.configureByText("config.yaml", """
                tuple_file: ./tuples.txt
                tuple_files: not-a-list
                """);
        myFixture.checkHighlighting();
    }

    public void testOpenFgaYamlExtensionIsValidated() {
        myFixture.configureByText(
                "store.openfga.yaml",
                """
                name: Test store
                tuple_file: <error descr="%s">./tuples.txt</error>
                """
                        .formatted(EXTENSION_ERROR));
        myFixture.checkHighlighting();
    }
}
