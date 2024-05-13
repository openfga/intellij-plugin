package dev.openfga.intellijplugin.util;

import org.jetbrains.annotations.NotNull;

import java.util.Enumeration;
import java.util.Iterator;

public class IterableEnumeration<T> implements Iterable<T> {
    private final Enumeration<T> enumeration;

    public IterableEnumeration(Enumeration<T> enumeration) {
        this.enumeration = enumeration;
    }

    public @NotNull Iterator<T> iterator() {
        return new Iterator<>() {
            public boolean hasNext() {
                return enumeration.hasMoreElements();
            }

            public T next() {
                return enumeration.nextElement();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static <T> Iterable<T> iterable(Enumeration<T> enumeration) {
        return new IterableEnumeration<>(enumeration);
    }
}