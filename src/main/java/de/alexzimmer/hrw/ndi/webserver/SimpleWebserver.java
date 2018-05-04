package de.alexzimmer.hrw.ndi.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleWebserver {

    private final int port;
    private final ServerSocket socket;

    public SimpleWebserver(int port) throws IOException {
        this.port = port;
        socket = new ServerSocket(port);
    }

    public void listen() throws IOException {
        while(true) {
            new ClientWorker(socket.accept()).run();
        }
    }

    public int getPort() {
        return port;
    }
}
