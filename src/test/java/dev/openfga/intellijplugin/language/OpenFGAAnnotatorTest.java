package dev.openfga.intellijplugin.language;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import java.util.List;

public class OpenFGAAnnotatorTest extends BasePlatformTestCase {

    private List<String> getErrors(String fileName, String text) {
        myFixture.configureByText(fileName, text);
        return myFixture.doHighlighting(HighlightSeverity.ERROR).stream()
                .map(HighlightInfo::getDescription)
                .toList();
    }

    public void testModelFileIsValidated() {
        List<String> errors = getErrors("model.fga", """
                model
                  schema 1.1
                type user
                type document
                  relations
                    define viewer: [user] or owner
                """);

        assertContainsElements(errors, "validation error at line=5, column=29: the relation `owner` does not exist.");
    }

    public void testValidModelFileHasNoErrors() {
        List<String> errors = getErrors("model.fga", """
                model
                  schema 1.1
                type user
                type document
                  relations
                    define owner: [user]
                    define viewer: [user] or owner
                """);

        assertEmpty(errors);
    }

    // Module files are only part of a modular model, so they are not validated as a complete model

    public void testModuleFileIsNotValidatedAsAModel() {
        List<String> errors = getErrors("issue-tracker.fga", """
                module issue-tracker

                extend type organization
                  relations
                    define can_create_project: owner

                type project
                  relations
                    define organization: [organization]
                    define viewer: member from organization
                """);

        assertEmpty(errors);
    }

    public void testModuleFileSyntaxErrorsAreReported() {
        List<String> errors = getErrors("issue-tracker.fga", """
                module issue-tracker

                type project
                  relations
                    define viewer: [user] and
                """);

        assertNotEmpty(errors);
    }
}
