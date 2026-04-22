import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Train {
    public static void main(String[] args) throws IOException {

        Backend backend = new Backend();
        PixelCanvas pixelCanvas = new PixelCanvas(32,32);
        pixelCanvas.backend = backend;
        pixelCanvas.scaleMultiplier = 15;
        pixelCanvas.setBackground(Color.BLACK);
        pixelCanvas.setSize(pixelCanvas.scaleMultiplier*pixelCanvas.canvasSize.width,
                pixelCanvas.scaleMultiplier*pixelCanvas.canvasSize.height);
        pixelCanvas.setLocation(0,0);
        File trainingFolder = new File("src/training");
        File[] itemFolders = trainingFolder.listFiles();

        System.out.println("Starting training...");
        for (int epochs = 0; epochs < 512; epochs++) {
            for (int item = 0; item < itemFolders.length; item++) {
                File itemFolder = new File("src/training/"+getNumberName(item));
                File[] examples = itemFolder.listFiles();

                backend.trainingSamples = new float[examples.length][pixelCanvas.defaultRes.width*pixelCanvas.defaultRes.height];
                backend.expectedOutputs = new float[examples.length][10];
                for (int i = 0; i < examples.length; i++) {
                    BufferedImage img = ImageIO.read(examples[i]);

                    float[] pixelArray = new float[pixelCanvas.defaultRes.width * pixelCanvas.defaultRes.height];
                    for (int y = 0; y < img.getHeight(); y++) {
                        for (int x = 0; x < img.getWidth(); x++) {
                            pixelArray[(y*pixelCanvas.canvasSize.width)+x] = new Color(img.getRGB(x,y)).getRed()/255.0f;
                        }
                    }

                    backend.trainingSamples[i] = pixelArray;
                    float[] label = new float[10];
                    label[item] = 1f;

                    backend.expectedOutputs[i] = label;
                }
                backend.train();
            }
            System.out.printf("\rEpoch "+epochs+" complete.");
        }

        System.out.printf("TRAINING DONE");
    }
    public static String getNumberName(int number){
        switch (number) {
            case 0: return "zero";
            case 1: return "one";
            case 2: return "two";
            case 3: return "three";
            case 4: return "four";
            case 5: return "five";
            case 6: return "six";
            case 7: return "seven";
            case 8: return "eight";
            case 9: return "nine";
        }
        return null;
    }
}
