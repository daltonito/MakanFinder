package sample;

import java.io.IOException;
import java.net.URISyntaxException;

public class Starter {

    private final static int MIN_HEAP = 1023;

    public static void main(String[] args) throws IOException, URISyntaxException {

        float heapSizeMegs = (Runtime.getRuntime().maxMemory()/1024)/1024;

        if (heapSizeMegs > MIN_HEAP) {
            Main.main(args);

        } else {
            String pathToJar = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            ProcessBuilder pb = new ProcessBuilder("java","-Xmx1024m", "-classpath", pathToJar, "sample.Main");
            pb.start();
        }
    }

}
