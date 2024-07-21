package dev.openfga.intellijplugin.parsing;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import dev.openfga.intellijplugin.OpenFGAFile;
import dev.openfga.intellijplugin.OpenFGALanguage;
import dev.openfga.intellijplugin.OpenFGALexerAdapter;
import dev.openfga.intellijplugin.psi.OpenFGATypes;
import org.jetbrains.annotations.NotNull;

public class OpenFGAParserDefinition implements ParserDefinition {

    public static final IFileElementType FILE = new IFileElementType(OpenFGALanguage.INSTANCE);

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new OpenFGALexerAdapter();
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return OpenFGATokenSets.SINGLE_LINE_COMMENT;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    //    @NotNull
    //    @Override
    //    public TokenSet getWhitespaceTokens() {
    //        return OpenFGATokenSets.WHITE_SPACE;
    //    }

    @NotNull
    @Override
    public PsiParser createParser(final Project project) {
        return new OpenFGAParser();
    }

    @NotNull
    @Override
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    @NotNull
    @Override
    public PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new OpenFGAFile(viewProvider);
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        return OpenFGATypes.Factory.createElement(node);
    }
}
