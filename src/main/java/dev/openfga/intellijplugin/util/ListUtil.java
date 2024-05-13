package dev.openfga.intellijplugin.util;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class ListUtil {

    public static <T> OptionalInt indexOf(List<T> list, Predicate<T> predicate) {
        return IntStream.range(0, list.size())
                .filter(i -> predicate.test(list.get(i)))
                .findFirst();
    }

    public static <T> Optional<T> get(List<T> list, Predicate<T> predicate) {
        var index = indexOf(list, predicate).orElse(-1);
        return index > -1
                ? Optional.ofNullable(list.get(index))
                : Optional.empty();
    }

    public static <T> void remove(List<T> list, Predicate<T> predicate) {
        indexOf(list, predicate).ifPresent(list::remove);
    }
}
