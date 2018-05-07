package de.alexzimmer.hrw.ndi.webserver.model;

import de.alexzimmer.hrw.ndi.webserver.exception.InvalidRequestException;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class HttpRequest {

    private final static Logger logger = Logger.getLogger(HttpRequest.class);

    private String method;
    private String uri;
    private String httpVersion;
    private String host;
    private HashMap<String, String> requestHeaders = new HashMap<>();

    @NotNull
    public HttpRequest(@NotNull List<String> raw) throws InvalidRequestException {
        String rawLine = raw.get(0);
        String[] splitted = rawLine.split(" ");
        this.method = splitted[0].toUpperCase();
        this.uri = splitted[1];
        this.httpVersion = splitted[2];
        raw.remove(0);

        for (String entry : raw) {
            String[] parts = entry.split(" ", 2);
            this.requestHeaders.put(parts[0].toLowerCase().replace(":", ""), parts[1]);
        }

        if ((this.host = requestHeaders.get("host")) == null) {
            throw new InvalidRequestException();
        }

        logger.info("Raw Request: "+rawLine);
        logger.info("Received " + this.method + " to resource \"" + this.uri + "\" for host " + this.host + " with HTTP-Version " + this.httpVersion);
    }

    @NotNull
    public static HttpRequest fromInputStream(@NotNull InputStream input) throws IOException {
        List<String> requestString = new LinkedList<>();
        StringBuffer buffer = new StringBuffer();
        int character;
        while((character = input.read()) != -1) {
            if(character == '\n') {
                if(buffer.length() <= 0) {
                    break; // finished reading the requests head
                } else {
                    requestString.add(buffer.toString());
                    buffer = new StringBuffer();
                }
            }
            else if(character != '\r') {
                buffer.append((char)character); // Append the character
            }
        }
        return new HttpRequest(requestString);
    }

    public String getMethod() {
        return method;
    }

    public String getURI() {
        return uri;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String getHost() {
        return host;
    }
}
