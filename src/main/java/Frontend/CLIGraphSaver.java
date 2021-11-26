package Frontend;

import Graphics.ImageWriter;

import java.io.IOException;

public class CLIGraphSaver {

    public void trySavingImage(int[] pixels) {
        try {
            int size = 512;
            String name = "test.png";
            ImageWriter writer = new ImageWriter();
            writer.writeImage(pixels, size, size, name);
        } catch (IOException e) {
            System.out.println("Image could not be saved");
            e.printStackTrace();
        }
    }
}
