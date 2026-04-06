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

        PixelCanvas pixelCanvas = new PixelCanvas(32,32);
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
                pixelCanvas.repaint();
            }
        });
        mainPanel.add(resetButton);

        window.setVisible(true);
        window.setResizable(false);

        Backend backend = new Backend();
        File zeroFolder = new File("src/training/zero");
        File[] zeros = zeroFolder.listFiles();

        backend.trainingSamples = new float[zeros.length][pixelCanvas.defaultRes.width*pixelCanvas.defaultRes.height];
        backend.expectedOutputs = new float[zeros.length][1];
        for (int i = 0; i < zeros.length; i++) {
            BufferedImage img = ImageIO.read(zeros[i]);

            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    pixelCanvas.pixels[y][x] = new Color(img.getRGB(x,y));
                }
            }

            backend.trainingSamples[i] = pixelCanvas.getPixels();
            backend.expectedOutputs[i] = new float[0];
        }
        backend.train();
    }
}
