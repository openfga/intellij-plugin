package dev.openfga.intellijplugin.util;

import java.net.URI;

public class UriUtil {

    public static URI resolve(URI baseUri, String path) {
        var normalizedBaseUri = new StringBuilder(baseUri.toString());

        if (!baseUri.toString().endsWith("/")) {
            normalizedBaseUri.append('/');
        }

        return URI.create(normalizedBaseUri.toString()).resolve(path);
    }
}
