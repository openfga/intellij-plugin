package dev.openfga.intellijplugin.language;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.ExternalAnnotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import dev.openfga.language.errors.DslErrorsException;
import dev.openfga.language.errors.ParsingError;
import dev.openfga.language.errors.StartEnd;
import dev.openfga.language.validation.ModelValidator;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.YAMLUtil;
import org.jetbrains.yaml.psi.YAMLFile;
import org.jetbrains.yaml.psi.impl.YAMLScalarListImpl;

import java.io.IOException;
import java.util.List;

public class OpenFGAStoreAnnotator extends ExternalAnnotator<String, List<? extends ParsingError>> {

    @Override
    public @Nullable String collectInformation(@NotNull final PsiFile file) {
        final Pair<PsiElement, String> modelField = getModelField(file);

        // If empty or not a scalar list, return
        if (!ObjectUtils.isNotEmpty(modelField) || !(modelField.getFirst() instanceof YAMLScalarListImpl)) {
            return null;
        }

        // When the field is in scalar strip model `getSecond()` returns a trimmed version of the model
        // This skews all the original line numbers, so we instead get the unmodified value
        if (ObjectUtils.isNotEmpty(modelField) && ObjectUtils.isNotEmpty(modelField.getFirst())) {
            // Remove first line which is the scalar notation ('|', '|-', '|+')
           return modelField.getFirst().getText().split("\n", 2)[1];
        }

        return null;
    }

    @Override
    public @Nullable List<? extends ParsingError> doAnnotate(@NotNull final String collectedInfo) {
        if (collectedInfo.isEmpty()) {
            return null;
        }

        try {
            ModelValidator.validateDsl(collectedInfo);
        } catch (IOException e) {
            throw new RuntimeException("Failure when attempting to validate model", e);
        } catch (DslErrorsException e) {
            return e.getErrors();
        }

        return null;
    }

    // Parsing is difficult: both the trimmed string (model) and the string retaining whitespace (originalString)
    // The model is the clean string, whereas the originalString is that from the YAML with extra whitespace
    // First the clean string is validated, then the original string is used to determine correct offsets
    @Override
    public void apply(@NotNull final PsiFile file,
                      final List<? extends ParsingError> annotationResult,
                      @NotNull final AnnotationHolder holder) {
        final @Nullable Pair<PsiElement, String> fileContents = getModelField(file);

        if (ObjectUtils.isEmpty(fileContents)) {
            return;
        }

        final PsiElement key = fileContents.getFirst();

        final String originalString = key.getText().split("\n", 2)[1];
        // Key offset and newline
        int offset = key.getFirstChild().getTextRange().getEndOffset() + 1;

        for (ParsingError error : annotationResult) {
            final StartEnd startEndLine = error.getLine();
            final StartEnd startEndColumn = error.getColumn();

            int offsetStart = getOffsetFromRange(
                    originalString, startEndLine.getStart(), startEndColumn.getStart(), offset);
            int offsetEnd = getOffsetFromRange(
                    originalString, startEndLine.getEnd(), startEndColumn.getEnd(), offset);

            holder.newAnnotation(HighlightSeverity.ERROR, error.getMessage())
                    .range(new TextRange(offsetStart, offsetEnd))
                    .create();
        }
    }

    private static int getOffsetFromRange(
            @NotNull final String doc, int line, int character, int offset) {
        final String[] lines = doc.split("\n");

        // Count offset
        for (int i = 0; i < line; i++) {
            // Strip leading spaces to normalize indentation, tabs are banned in YAML.
            // This assumes an indent of 1 || 2 spaces, doesn't support 4 as that would bite into indentation
            final String replacementLine = lines[i].replaceFirst("^ {1,2}", "");
            offset += replacementLine.length() + (lines[i].length() - replacementLine.length()) + 1;
        }

        return offset + character;
    }

    private static @Nullable Pair<PsiElement, String> getModelField(@NotNull final PsiFile file) {
        final Pair<PsiElement, String> field = YAMLUtil.getValue((YAMLFile) file, "model");

        if (ObjectUtils.isNotEmpty(field)) {
            return field;
        }
        return null;
    }
}
