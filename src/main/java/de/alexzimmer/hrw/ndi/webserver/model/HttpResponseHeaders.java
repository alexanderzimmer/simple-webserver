package de.alexzimmer.hrw.ndi.webserver.model;

import java.util.Collection;
import java.util.HashMap;

public class HttpResponseHeaders {

    private HashMap<String, String> responseHeaders = new HashMap<>();

    public static String formatKey(String keyString) {
        char[] key = keyString.toCharArray();
        boolean nextUppcercase = false;
        key[0] = Character.toUpperCase(key[0]);
        for (int i = 1; i < key.length; i++) {
            if (nextUppcercase) {
                key[i] = Character.toUpperCase(key[i]);
                nextUppcercase = false;
                continue;
            }
            if (key[i] == '-') {
                nextUppcercase = true;
                continue;
            }
            key[i] = Character.toLowerCase(key[i]);
        }
        return new String(key);
    }

    public void put(String key, String value) {
        this.responseHeaders.put(formatKey(key), value);
    }

    public String get(String key) {
        return this.responseHeaders.get(formatKey(key));
    }

    public Collection<String> getKeys() {
        return responseHeaders.keySet();
    }

}
