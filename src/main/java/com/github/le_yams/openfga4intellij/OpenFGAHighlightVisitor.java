package com.github.le_yams.openfga4intellij;

import com.github.le_yams.openfga4intellij.psi.OpenFGATypes;
import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.daemon.impl.HighlightInfoType;
import com.intellij.codeInsight.daemon.impl.HighlightVisitor;
import com.intellij.codeInsight.daemon.impl.analysis.HighlightInfoHolder;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OpenFGAHighlightVisitor implements HighlightVisitor {

    private HighlightInfoHolder myHolder;
    private OpenFGASyntaxHighlighter openfgaHighlighter;

    @NotNull
    protected OpenFGASyntaxHighlighter getHighlighter() {
        return openfgaHighlighter;
    }

    @Override
    public boolean suitableForFile(@NotNull PsiFile file) {
        return file.getLanguage().isKindOf(OpenFGALanguage.INSTANCE);
    }

    @Override
    public void visit(@NotNull PsiElement element) {
        ASTNode node = element.getNode();
        IElementType elementType = node.getElementType();

        HighlightInfo info = getHighlightInfo(element, elementType);
        if (info != null) {
            myHolder.add(info);
        }
    }

    @Override
    public @NotNull HighlightVisitor clone() {
        return new OpenFGAHighlightVisitor();
    }

    private @Nullable HighlightInfo getHighlightInfo(PsiElement element, IElementType elementType) {

        TextAttributesKey[] attributesKeys = getHighlighter().getTokenHighlights(elementType);
        if (attributesKeys.length == 0) {
            return null;
        }

        HighlightInfo.Builder builder = HighlightInfo.newHighlightInfo(HighlightInfoType.TEXT_ATTRIBUTES);

        for (var attributesKey : attributesKeys) {
            builder = builder.textAttributes(attributesKey);

        }

        return builder.range(element).create();
    }

    @Override
    public boolean analyze(@NotNull PsiFile file, boolean updateWholeFile, @NotNull HighlightInfoHolder holder, @NotNull Runnable action) {
        myHolder = holder;
        openfgaHighlighter = new OpenFGASyntaxHighlighter();
        try {
            action.run();
        } finally {
            myHolder = null;
            openfgaHighlighter = null;
        }
        return true;
    }

}
