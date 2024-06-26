package dev.openfga.intellijplugin.parser;

import dev.openfga.intellijplugin.parsing.OpenFGAParserDefinition;
import com.intellij.testFramework.ParsingTestCase;

public class OpenFGAParserTest extends ParsingTestCase {

    public OpenFGAParserTest() {
        super("", "fga", new OpenFGAParserDefinition());
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/testData";
    }

    public void testschema() throws Throwable {
        doCodeTest("""
                 model
                   schema 1.1
                 """);
    }

    public void testnoschema() throws Throwable {
        doCodeTest("""
                 model
                 """);
    }

    public void testuser() throws Throwable {
        doCodeTest("""
                 model
                   schema 1.1
                 type user
                 """);
    }

}