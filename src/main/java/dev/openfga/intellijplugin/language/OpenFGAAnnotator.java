package dev.openfga.intellijplugin.language;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.ExternalAnnotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import dev.openfga.language.errors.DslErrorsException;
import dev.openfga.language.errors.ParsingError;
import dev.openfga.language.errors.StartEnd;
import dev.openfga.language.validation.DslValidator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

public class OpenFGAAnnotator extends ExternalAnnotator<String, List<? extends ParsingError>> {

    @Override
    public @Nullable String collectInformation(@NotNull PsiFile file) {
        return file.getText();
    }

    @Override
    public @Nullable List<? extends ParsingError> doAnnotate(@NotNull final String collectedInfo) {
        try {
            DslValidator.validate(collectedInfo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (DslErrorsException e) {
            return e.getErrors();
        }

        return null;
    }

    @Override
    public void apply(@NotNull final PsiFile file,
                      final List<? extends ParsingError> annotationResult,
                      @NotNull final AnnotationHolder holder) {

        final String fileContents = file.getText();

        for (ParsingError error : annotationResult) {
            final StartEnd startEndLine = error.getLine();
            final StartEnd startEndColumn = error.getColumn();

            int offsetStart = getOffsetFromRange(fileContents, startEndLine.getStart(), startEndColumn.getStart());
            int offsetEnd = getOffsetFromRange(fileContents, startEndLine.getEnd(), startEndColumn.getEnd());

            holder.newAnnotation(HighlightSeverity.ERROR, error.getFullMessage())
                    .range(new TextRange(offsetStart, offsetEnd))
                    .create();
        }
    }

    private static int getOffsetFromRange(@NotNull final String doc, int line, int character) {
        int offset = 0;

        final String[] lines = doc.split("\n");

        for (int i = 0; i < line; i++) {
            offset += lines[i].length() + 1;
        }

        offset += character;

        return offset;
    }
}
