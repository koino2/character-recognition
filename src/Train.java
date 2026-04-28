import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Train {

    static class TrainingPair {
        File file;
        int label;
        public TrainingPair(File f, int l){
            file = f;
            label = l;
        }
    }
    public static String path = "weights.txt";

    public static void main(String[] args) throws IOException {
        Backend backend = new Backend();
        PixelCanvas pixelCanvas = new PixelCanvas(28, 28);
        pixelCanvas.backend = backend;
        pixelCanvas.scaleMultiplier = 15;
        pixelCanvas.setBackground(Color.BLACK);
        pixelCanvas.setSize(pixelCanvas.scaleMultiplier * pixelCanvas.canvasSize.width,
                pixelCanvas.scaleMultiplier * pixelCanvas.canvasSize.height);
        pixelCanvas.setLocation(0, 0);
        File trainingFolder = new File("training");
        File[] itemFolders = trainingFolder.listFiles();

        System.out.println("Starting training...");
        for (int epochs = 0; epochs < 64; epochs++) {

            List<File> samples = new ArrayList<>();
            List<Integer> labels = new ArrayList<>();

            for (int i = 0; i < itemFolders.length; i++) {
                int item = i;
                File itemFolder = new File("training/"+getNumberName(item));
                File[] items = itemFolder.listFiles();
                for (int j = 0; j < items.length; j++) {
                    samples.add(items[j]);
                    labels.add(item);
                }
            }

            List<TrainingPair> trainingPairs = new ArrayList<>();
            for (int i = 0; i < samples.size(); i++) {
                trainingPairs.add(new TrainingPair(samples.get(i), labels.get(i)));
            }

            Collections.shuffle(trainingPairs);

            File[] examples = new File[samples.size()];
            for (int i = 0; i < samples.size(); i++) {
                examples[i] = trainingPairs.get(i).file;
            }

            backend.trainingSamples = new float[examples.length][pixelCanvas.defaultRes.width * pixelCanvas.defaultRes.height];
            backend.expectedOutputs = new float[examples.length][10];

            for (int i = 0; i < examples.length; i++) {
                BufferedImage img = ImageIO.read(examples[i]);

                float[] pixelArray = new float[pixelCanvas.defaultRes.width * pixelCanvas.defaultRes.height];
                for (int y = 0; y < img.getHeight(); y++) {
                    for (int x = 0; x < img.getWidth(); x++) {
                        pixelArray[(y * pixelCanvas.canvasSize.width) + x] = new Color(img.getRGB(x, y)).getRed() / 255.0f;
                    }
                }

                backend.trainingSamples[i] = pixelArray;
                float[] label = new float[10];
                int item = trainingPairs.get(i).label;
                label[item] = 1f;

                backend.expectedOutputs[i] = label;
            }
            backend.train();

            System.out.printf("\rEpoch " + epochs + " complete.");
        }

        System.out.println("\nTRAINING DONE");

        float[] weights = backend.getWeights();

        try (DataOutputStream out = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(path)) // wow.
        )) {

            out.writeInt(weights.length);
            for (int i = 0; i < weights.length; i++) {
                out.writeFloat(weights[i]);
            }
        }
        System.out.println("\nSaved: " + weights.length);
    }

    public static String getNumberName(int number) {
        switch (number) {
            case 0:
                return "zero";
            case 1:
                return "one";
            case 2:
                return "two";
            case 3:
                return "three";
            case 4:
                return "four";
            case 5:
                return "five";
            case 6:
                return "six";
            case 7:
                return "seven";
            case 8:
                return "eight";
            case 9:
                return "nine";
        }
        return null;
    }
}
