import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        JFrame window = new JFrame();
        window.setTitle("Handwritten character recognition");
        window.setSize(new Dimension(1200,800));
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setLocation(0,0);
        mainPanel.setSize(new Dimension(1200,800));
        window.setContentPane(mainPanel);
        mainPanel.setBackground(new Color(17, 18, 28));

        Backend backend = new Backend();

        PixelCanvas pixelCanvas = new PixelCanvas(32,32);
        pixelCanvas.backend = backend;
        pixelCanvas.scaleMultiplier = 15;
        pixelCanvas.setBackground(Color.BLACK);
        pixelCanvas.setSize(pixelCanvas.scaleMultiplier*pixelCanvas.canvasSize.width,
                pixelCanvas.scaleMultiplier*pixelCanvas.canvasSize.height);
        pixelCanvas.setLocation(0,0);
        mainPanel.add(pixelCanvas);

        JButton resetButton = new JButton();
        resetButton.setLocation((int) pixelCanvas.getLocation().getX() + 20, pixelCanvas.getHeight()+50);
        resetButton.setSize(100,30);
        resetButton.setText("Reset");
        resetButton.setVisible(true);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pixelCanvas.init();
                backend.update(pixelCanvas);
                pixelCanvas.repaint();
            }
        });
        mainPanel.add(resetButton);

        window.setVisible(true);
        window.setResizable(false);

        File trainingFolder = new File("src/training");
        File[] itemFolders = trainingFolder.listFiles();

        for (int item = 0; item < itemFolders.length; item++) {
            File itemFolder = new File("src/training/"+getNumberName(item));
            File[] examples = itemFolder.listFiles();

            backend.trainingSamples = new float[examples.length][pixelCanvas.defaultRes.width*pixelCanvas.defaultRes.height];
            backend.expectedOutputs = new float[examples.length][10];
            for (int i = 0; i < examples.length; i++) {
                BufferedImage img = ImageIO.read(examples[i]);

                for (int y = 0; y < img.getHeight(); y++) {
                    for (int x = 0; x < img.getWidth(); x++) {
                        pixelCanvas.pixels[y][x] = new Color(img.getRGB(x,y));
                    }
                }

                backend.trainingSamples[i] = pixelCanvas.getPixels();
                float[] label = new float[10];
                label[item] = 1f;

                backend.expectedOutputs[i] = label;
            }
            backend.train();
        }

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
