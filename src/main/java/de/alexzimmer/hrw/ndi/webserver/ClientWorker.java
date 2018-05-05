package de.alexzimmer.hrw.ndi.webserver;

import de.alexzimmer.hrw.ndi.webserver.model.HttpRequest;
import de.alexzimmer.hrw.ndi.webserver.model.HttpResponse;

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
            this.response = new HttpResponse(request);
            this.response.writeResponse(output);
        } catch (Exception e) {
        } finally {
            try {
                output.close();
            } catch (Exception e) {
            }
        }
    }
}
