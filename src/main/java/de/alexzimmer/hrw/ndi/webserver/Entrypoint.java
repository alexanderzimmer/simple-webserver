package de.alexzimmer.hrw.ndi.webserver;

public class Entrypoint {

    public static void main(String[] args) {

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.exit(0);
            }
        });

        try {
            int port = 8080;
            if(args != null && args.length >= 1) {
                port = Integer.getInteger(args[0]);
            }
            new SimpleWebserver(port).listen();
        } catch(Exception e) {
            System.err.print("Error: "+e.toString());
            System.exit(1);
        }
    }

}
