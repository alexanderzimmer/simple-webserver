package de.alexzimmer.hrw.ndi.webserver.model;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HttpResponse {

    private static final String contentDirName = "content";

    private static final String httpVersion = "Http/1.1";
    private static final String serverVersion = "Server: SimpleWebserver/1.0";
    private static final String newLine = "\r\n";
    private static MimetypesFileTypeMap mimeTypes = new MimetypesFileTypeMap();

    private String status;
    private HttpResponseHeaders responseHeaders = new HttpResponseHeaders();
    private String resource;
    private String resourcePath;
    private File responseFile;

    public HttpResponse(HttpRequest request) {
        this.resource = request.getURI();
        if (this.resource.equals("/")) {
            this.resource = request.getURI() + "/index.html";
        }
        this.resourcePath = fixPath(resource);

        this.responseFile = new File(resourcePath);
        if (!this.responseFile.exists() || !this.responseFile.isFile()) {
            this.status = "404 Not Found";
            this.responseFile = null;
        } else {
            this.status = "200 Ok";
            responseHeaders.put("Content-Type", mimeTypes.getContentType(responseFile));
            responseHeaders.put("Content-Length", String.valueOf(responseFile.length()));
        }
    }

    public static String fixPath(String resource) {
        String seperator = System.getProperty("file.separator", "/");
        StringBuffer buffer = new StringBuffer();
        buffer.append(System.getProperty("user.dir") + seperator + contentDirName);
        for (int i = 0; i < resource.length(); i++) {
            char character = resource.charAt(i);
            if (character == '/') {
                buffer.append(seperator);
            } else {
                buffer.append(character);
            }
        }
        return buffer.toString();
    }

    public void writeResponse(PrintStream stream) throws IOException {
        stream.print(httpVersion + " " + this.status + newLine);
        stream.print(serverVersion + newLine);
        for (String value : responseHeaders.getKeys()) {
            stream.print(value + ": " + responseHeaders.get(value) + newLine);
        }
        if (responseFile != null) {
            stream.print(newLine);
            for (String line : Files.readAllLines(Paths.get(resourcePath))) {
                stream.println(line);
            }
        }
    }

}
