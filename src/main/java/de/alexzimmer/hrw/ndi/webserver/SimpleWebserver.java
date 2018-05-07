package de.alexzimmer.hrw.ndi.webserver;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class SimpleWebserver {

    private final static Logger logger = Logger.getLogger(SimpleWebserver.class);

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
        logger.info("Finished Listening - Server is ready for shutdown");
    }

    public int getPort() {
        return port;
    }
}
