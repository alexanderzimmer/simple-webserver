package de.alexzimmer.hrw.ndi.webserver;

import org.apache.log4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

public class Entrypoint {

    private final static Logger logger = Logger.getLogger(Entrypoint.class);

    public static AtomicBoolean keepRunning = new AtomicBoolean(true);
    private static SimpleWebserver server;

    public static void main(String[] args) {

        final Thread mainThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                Entrypoint.keepRunning.set(false);
            }
        });

        try {
            int port = 8080;
            if(args != null && args.length >= 1) {
                port = Integer.getInteger(args[0]);
            }
            server = new SimpleWebserver(port);
            server.listen();
        } catch(Exception e) {
            logger.info("Exception while starting server", e);
            Entrypoint.keepRunning.set(false);
            System.exit(1);
        }
    }

}
