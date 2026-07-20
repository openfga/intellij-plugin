package dev.openfga.intellijplugin.language;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import dev.openfga.intellijplugin.OpenFGAStoreFileType;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLScalar;
import org.jetbrains.yaml.psi.YAMLSequence;
import org.jetbrains.yaml.psi.YAMLSequenceItem;
import org.jetbrains.yaml.psi.YAMLValue;

/**
 * Validates the {@code tuple_file} and {@code tuple_files} fields in OpenFGA store files.
 *
 * <p>{@code tuples}, {@code tuple_file} and {@code tuple_files} may be used together or
 * individually, both in the global scope and in test scopes. All of them are optional, so no
 * error is reported when none is set.
 *
 * <p>When {@code tuple_file} or {@code tuple_files} are specified, the referenced files must
 * use one of the supported extensions: {@code json}, {@code jsonl}, {@code yaml}, {@code yml}
 * or {@code csv}.
 */
public class OpenFGATupleFileAnnotator implements Annotator {

    public static final List<String> SUPPORTED_EXTENSIONS = List.of("json", "jsonl", "yaml", "yml", "csv");

    private static final Set<String> SUPPORTED_EXTENSIONS_SET = Set.copyOf(SUPPORTED_EXTENSIONS);

    private static final String TUPLE_FILE_KEY = "tuple_file";
    private static final String TUPLE_FILES_KEY = "tuple_files";

    private static final String UNSUPPORTED_EXTENSION_MESSAGE = "Tuple file must have one of the following extensions: "
            + String.join(
                    ", ", SUPPORTED_EXTENSIONS.stream().map(ext -> "." + ext).toList());

    @Override
    public void annotate(@NotNull final PsiElement element, @NotNull final AnnotationHolder holder) {
        if (!(element instanceof YAMLKeyValue keyValue)
                || !OpenFGAStoreFileType.isStoreFile(element.getContainingFile())) {
            return;
        }

        switch (keyValue.getKeyText()) {
            case TUPLE_FILE_KEY -> annotateTupleFile(keyValue, holder);
            case TUPLE_FILES_KEY -> annotateTupleFiles(keyValue, holder);
            default -> {
                // Not a tuple file field, nothing to validate
            }
        }
    }

    private void annotateTupleFile(final YAMLKeyValue keyValue, final AnnotationHolder holder) {
        final YAMLValue value = keyValue.getValue();

        // The field is optional and may be left empty while editing
        if (value == null) {
            return;
        }

        if (!(value instanceof YAMLScalar scalar)) {
            holder.newAnnotation(HighlightSeverity.ERROR, "tuple_file must be a single file path")
                    .range(value)
                    .create();
            return;
        }

        annotateTupleFileReference(scalar, holder);
    }

    private void annotateTupleFiles(final YAMLKeyValue keyValue, final AnnotationHolder holder) {
        final YAMLValue value = keyValue.getValue();

        // The field is optional and may be left empty while editing
        if (value == null) {
            return;
        }

        if (!(value instanceof YAMLSequence sequence)) {
            holder.newAnnotation(HighlightSeverity.ERROR, "tuple_files must be a list of file paths")
                    .range(value)
                    .create();
            return;
        }

        for (YAMLSequenceItem item : sequence.getItems()) {
            final YAMLValue itemValue = item.getValue();

            if (itemValue == null) {
                continue;
            }

            if (!(itemValue instanceof YAMLScalar scalar)) {
                holder.newAnnotation(HighlightSeverity.ERROR, "tuple_files entries must be file paths")
                        .range(itemValue)
                        .create();
                continue;
            }

            annotateTupleFileReference(scalar, holder);
        }
    }

    private void annotateTupleFileReference(final YAMLScalar scalar, final AnnotationHolder holder) {
        final String path = scalar.getTextValue().trim();

        if (path.isEmpty()) {
            return;
        }

        if (!hasSupportedExtension(path)) {
            holder.newAnnotation(HighlightSeverity.ERROR, UNSUPPORTED_EXTENSION_MESSAGE)
                    .range(scalar)
                    .create();
            return;
        }

        final VirtualFile baseDir = getContainingDirectory(scalar);

        // If the store file is not backed by a real directory (e.g. an in-memory
        // scratch file), the reference cannot be resolved, so don't report errors.
        if (baseDir == null) {
            return;
        }

        if (findReferencedFile(path, baseDir) == null) {
            holder.newAnnotation(HighlightSeverity.ERROR, "Tuple file not found: " + path)
                    .range(scalar)
                    .create();
        }
    }

    private static boolean hasSupportedExtension(final String path) {
        final int extensionIndex = path.lastIndexOf('.');

        if (extensionIndex < 0) {
            return false;
        }

        final String extension = path.substring(extensionIndex + 1).toLowerCase(Locale.ROOT);

        return SUPPORTED_EXTENSIONS_SET.contains(extension);
    }

    private static @Nullable VirtualFile getContainingDirectory(final YAMLScalar scalar) {
        final PsiFile containingFile = scalar.getContainingFile();

        if (containingFile == null) {
            return null;
        }

        final VirtualFile virtualFile = containingFile.getOriginalFile().getVirtualFile();

        if (virtualFile == null) {
            return null;
        }

        return virtualFile.getParent();
    }

    private static @Nullable VirtualFile findReferencedFile(final String path, final VirtualFile baseDir) {
        if (FileUtil.isAbsolute(path)) {
            return LocalFileSystem.getInstance().findFileByPath(path);
        }

        return baseDir.findFileByRelativePath(FileUtil.toSystemIndependentName(path));
    }
}
