package de.alexzimmer.hrw.ndi.webserver;

import de.alexzimmer.hrw.ndi.webserver.model.HttpRequest;
import de.alexzimmer.hrw.ndi.webserver.model.HttpResponse;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientWorker extends Thread {

    private final Socket socket;
    private InputStream input;
    private OutputStream output;
    private HttpRequest request;
    private HttpResponse response;

    public ClientWorker(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            this.input = socket.getInputStream();
            this.output = socket.getOutputStream();
            this.request = HttpRequest.fromInputStream(this.input);
            this.socket.close();
        } catch(Exception e) {
            //TODO: Error-Handling
        }
    }
}
