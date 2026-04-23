import ai.Network;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;

public class Main {

    public static String path = "weights.txt";

    public static void main(String[] args) throws IOException {
        JFrame window = new JFrame();
        window.setTitle("Handwritten character recognition");
        window.setSize(new Dimension(800,600));
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

        try(DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(path)))){ // gentle reminder that java is the most popular programming language and runs on more than 3 billion devices
            int size = in.readInt();
            System.out.println("Loaded: " + size);

            for (int i = 1; i < backend.network.layers.length; i++) {
                for (int j = 0; j < backend.network.layers[i].neurons.length; j++) {
                    for (int k = 0; k < backend.network.layers[i].neurons[j].incomingWeights.length; k++) {
                        backend.network.layers[i].neurons[j].incomingWeights[k].weight = in.readFloat();
                    }
                }
            }
        }

        window.setVisible(true);
        window.setResizable(false);

    }
}
