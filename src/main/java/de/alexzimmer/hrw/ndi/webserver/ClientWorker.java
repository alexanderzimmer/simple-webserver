package de.alexzimmer.hrw.ndi.webserver;

import de.alexzimmer.hrw.ndi.webserver.exception.InvalidRequestException;
import de.alexzimmer.hrw.ndi.webserver.model.HttpRequest;
import de.alexzimmer.hrw.ndi.webserver.model.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;

public class ClientWorker extends Thread {

    private final Socket socket;
    private InputStream input;
    private PrintStream output;
    private HttpRequest request;
    private HttpResponse response;

    public ClientWorker(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            this.input = socket.getInputStream();
            this.output = new PrintStream(socket.getOutputStream());
            this.request = HttpRequest.fromInputStream(this.input);
            this.output.print(request.getHttpVersion() + " 418 I'm a teapot");
        } catch (IOException e) {
            this.output.print(request.getHttpVersion() + " 500 Internal Server Error");
        } catch (InvalidRequestException e) {
            this.output.print(request.getHttpVersion() + " 400 Bad Request ");
        }
    }
}
