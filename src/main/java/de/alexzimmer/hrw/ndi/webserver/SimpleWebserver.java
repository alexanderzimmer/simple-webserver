package de.alexzimmer.hrw.ndi.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class SimpleWebserver {

    private final int port;
    private final ServerSocket socket;

    public SimpleWebserver(int port) throws IOException {
        this.port = port;
        socket = new ServerSocket(port);
        socket.setSoTimeout(1000);
    }

    public void listen() throws IOException {
        while(Entrypoint.keepRunning.get() == true) {
            try {
                new ClientWorker(socket.accept()).run();
            } catch(SocketTimeoutException e) {}
        }
        System.out.println("Finished Listening!");
    }

    public int getPort() {
        return port;
    }
}
