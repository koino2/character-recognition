package examples;

import ai.*;
import ai.modules.networkeditor.tools.ImageRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

public class RandomNetwork {
    private static Random random = new Random();
    private static Network network;
    private static BufferedImage image;
    private static JFrame frame;
    private static JPanel panel;

    public static void main(String[] args) {
        createNetwork();

        frame = new JFrame("Image Renderer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (image != null) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                    int panelWidth = getWidth();
                    int panelHeight = getHeight();
                    double aspectRatio = (double) image.getWidth() / image.getHeight();

                    int newWidth, newHeight;
                    if ((double) panelWidth / panelHeight > aspectRatio) {
                        newHeight = panelHeight;
                        newWidth = (int) (panelHeight * aspectRatio);
                    } else {
                        newWidth = panelWidth;
                        newHeight = (int) (panelWidth / aspectRatio);
                    }

                    int x = (panelWidth - newWidth) / 2;
                    int y = (panelHeight - newHeight) / 2;

                    g2d.setColor(Color.BLACK);
                    g2d.fillRect(0,0,getWidth(),getHeight());
                    g2d.drawImage(image, x, y, newWidth, newHeight, this);

                    g.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    g.setColor(Color.WHITE);
                }
            }
        };

        frame.add(panel);
        frame.setVisible(true);

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    createNetwork();
                    panel.repaint();
                }
            }
        });
    }

    private static void createNetwork() {
        int layerCount = random.nextInt(2, 20);

        int[] neuronAmounts = new int[layerCount];
        for (int i = 0; i < layerCount; i++) {
            neuronAmounts[i] = new Random().nextInt(2,20);
        }
        network = CytonAI.networkBuilder(neuronAmounts, -1.1f, 1.1f);

        network.compute();
        image = ImageRenderer.renderImage(network);
    }
}
