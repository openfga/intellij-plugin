package dev.openfga.intellijplugin.parser;

import dev.openfga.intellijplugin.parsing.OpenFGAParserDefinition;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.ParsingTestCase;

public class OpenFGAParserTest extends ParsingTestCase {

    public OpenFGAParserTest() {
        super("", "fga", new OpenFGAParserDefinition());
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/testData";
    }

    // Valid models

    public void testSchema() throws Throwable {
        doCodeTest("""
                 model
                   schema 1.1
                 """);
    }

    public void testUser() throws Throwable {
        doCodeTest("""
                 model
                   schema 1.1
                 type user
                 """);
    }

    public void testMultipleTypes() throws Throwable {
        doCodeTest("""
                 model
                   schema 1.1
                 type user
                 type admin
                 type folder
                 """);
    }

    public void testSpacing() throws Throwable {
        doCodeTest("""
                 model
                   schema 1.1
                 
                 type user
                 
                 type admin
                 
                 type folder
                 """);
    }

    public void testRelation() throws Throwable {
        doCodeTest("""
                 model
                   schema 1.1
                 
                 type user
                    relations
                        define admin: [user]
                 """);
    }

    public void testBlankLineWithWhitespace() throws Throwable {
        // Regression test for #163: a whitespace-only blank line must not be
        // tokenized as indentation.
        String code = "model\n"
                + "  schema 1.1\n"
                + "type company\n"
                + "  relations\n"
                + "    define user: [user]\n"
                + "    \n" // blank line with trailing whitespace
                + "    define bla: user\n";
        PsiFile file = createPsiFile("blankLineWithWhitespace", code);
        ensureParsed(file);
        assertNull(
                "Model with a whitespace-only blank line should parse without errors",
                PsiTreeUtil.findChildOfType(file, PsiErrorElement.class));
    }

    public void testTrailingWhitespaceOnlyLineAtEof() throws Throwable {
        // Regression test for #163: a whitespace-only final line with no
        // trailing newline must not be tokenized as indentation.
        String code = "model\n"
                + "  schema 1.1\n"
                + "type company\n"
                + "  relations\n"
                + "    define user: [user]\n"
                + "    "; // whitespace-only final line, EOF right after
        PsiFile file = createPsiFile("trailingWhitespaceOnlyLineAtEof", code);
        ensureParsed(file);
        assertNull(
                "Model ending with a whitespace-only line should parse without errors",
                PsiTreeUtil.findChildOfType(file, PsiErrorElement.class));
    }

    public void testComments() throws Throwable {
        doCodeTest("""
                 # This is a sample model
                 model
                   schema 1.1
                 # This is a comment that should be allowed
                 type user
                   relations
                     # This is a comment that should be allowed
                     define admin: [user]
                 """);
    }

    // Invalid models

    public void testNoSchemaInvalid() throws Throwable {
        doCodeTest("""
                 model
                 """);
    }

    public void testSchemaInvalid() throws Throwable {
        doCodeTest("""
                model
                  schema a.b
                """);
    }

    public void testMissingRelationsInvalid() throws Throwable {
        doCodeTest("""
                 model
                   schema 1.1
                 
                 type user
                    define admin: [user]
                 """);
    }

    public void testMixedOperatorsInvalid() throws Throwable {
        doCodeTest("""
                 model
                   schema 1.1
                  type user
                    relations
                      define own: [user]
                      define follower: [user]
                      define blocked: [user]
                      define viewer: [user] and follower or own but not blocked
                 """);
    }

    public void testMixedOperatorsWithParensInvalid() throws Throwable {
        doCodeTest("""
                 model
                   schema 1.1
                  type user
                    relations
                      define own: [user]
                      define follower: [user]
                      define blocked: [user]
                      define viewer: [user] and (follower or own) but not blocked
                 """);
    }

    public void testMixedOperatorsWithMixedParensInvalid() throws Throwable {
        doCodeTest("""
                model
                  schema 1.1
                type user
                  relations
                    define follower: [user]
                    define blocked: [user]
                    define restricted: [user]
                    define viewer: [user] and (follower but not blocked but not restricted)
                """);
    }

    public void testMixOfVersionsShouldInvalid() throws Throwable {
        doCodeTest("""
                model
                  schema 1.1
                type user
                type org
                  relations
                    define member: [user]
                type group
                  relations
                    define parent as self
                    define viewer as viewer from parent
                """);
    }

    public void testEmptyDirectlyAssignableWithSpaceInvalid() throws Throwable {
        doCodeTest("""
                model
                  schema 1.1
                type user
                type org
                  relations
                    define member: [ ]
                    define reader: [user]
                """);
    }

//    public void testInlineComments() throws Throwable {
//        doCodeTest("""
//                 model
//                   schema 1.1
//                 type user # This is a comment that should be allowed
//                   relations # This is a comment that should be allowed
//                     define admin: [user] # This is a comment that should be allowed
//                 """);
//    }

}