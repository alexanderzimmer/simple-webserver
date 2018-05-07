package de.alexzimmer.hrw.ndi.webserver.model;

import org.apache.log4j.Logger;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HttpResponse {

    private final static Logger logger = Logger.getLogger(HttpResponse.class);
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
            logger.info("Resource " + resource + " not found in path \"" + resourcePath + "\"");
            this.responseFile = null;
        } else {
            logger.debug("Resource " + resource + " found in path \"" + resourcePath + "\"");
            this.status = "200 Ok";
            responseHeaders.put("Content-Type", mimeTypes.getContentType(responseFile));
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
        StringBuffer responseBuffer = new StringBuffer();
        stream.print(httpVersion + " " + this.status + newLine);
        responseBuffer.append(httpVersion + " " + this.status + "\n");
        stream.print(serverVersion + newLine);
        responseBuffer.append(serverVersion + "\n");
        if (this.status.equals("200 Ok")) {
            for (String value : responseHeaders.getKeys()) {
                responseBuffer.append(value + ": " + responseHeaders.get(value) + "\n");
                stream.print(value + ": " + responseHeaders.get(value) + newLine);
            }
            if (responseFile != null) {
                stream.print(newLine);
                responseBuffer.append("\n");
                for (String line : Files.readAllLines(Paths.get(resourcePath))) {
                    responseBuffer.append(line + "\n");
                    stream.println(line);
                }
            }
        } else {
            stream.print(newLine);
            responseBuffer.append("\n");
            stream.print(status);
            responseBuffer.append(status);
        }
        logger.info(
                "Responded to GET request on resource \"" + resource + "\" with status " + status + "\n" +
                "RESPONSE WAS SENT TO OUTPUT: >>>>>>\n" +
                responseBuffer.toString() +
                "\nRESPONSE WAS SENT TO OUTPUT: <<<<<<"
        );
    }

}
