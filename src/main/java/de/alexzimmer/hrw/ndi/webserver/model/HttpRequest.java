package de.alexzimmer.hrw.ndi.webserver.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class HttpRequest {

    private String method;
    private String uri;
    private String httpVersion;
    private String host;

    public static HttpRequest fromInputStream(InputStream input) throws IOException {
        List<String> requestString = new LinkedList<String>();
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

    public HttpRequest(List<String> raw) {
        String line = raw.get(0);
        String[] splitted = line.split(" ");
        this.method = splitted[0].toUpperCase();
        this.uri = splitted[1];
        this.httpVersion = splitted[2];
        this.host = raw.get(1).split(" ")[1];
        System.out.println("Received "+this.method+" to resource \""+this.uri+"\" for host "+this.host+" with HTTP-Version "+this.httpVersion);
    }

}
