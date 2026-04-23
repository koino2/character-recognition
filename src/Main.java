import ai.CytonAI;
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
    public static String savePath = "training/";

    public static void main(String[] args) throws IOException {
        JFrame window = new JFrame();
        window.setTitle("Handwritten character recognition");
        window.setSize(new Dimension(750,600));
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
        resetButton.setLocation((int) pixelCanvas.getLocation().getX() + 20, pixelCanvas.getHeight()+20);
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

        JSpinner selector = new JSpinner();
        selector.setLocation(150, pixelCanvas.getHeight()+20);
        selector.setSize(50,30);
        selector.setVisible(true);
        selector.setFont(new Font("Segoe UI Bold", Font.BOLD, 20));
        mainPanel.add(selector);

        JButton addSample = new JButton("Add Sample");
        addSample.setLocation(225, pixelCanvas.getHeight()+20);
        addSample.setSize(100,30);
        addSample.setVisible(true);
        addSample.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((Integer) selector.getValue() > 9 || (Integer) selector.getValue() < 0){
                    System.out.println("Invalid input!");
                    return;
                }
                pixelCanvas.saveImage(savePath+Train.getNumberName((Integer) selector.getValue()));
                pixelCanvas.init();
                backend.update(pixelCanvas);
                pixelCanvas.repaint();
            }
        });
        mainPanel.add(addSample);

        OutputPanel outputPanel = new OutputPanel(backend);
        outputPanel.setLayout(null);
        outputPanel.setSize(240,540);
        outputPanel.setLocation(490,10);
        outputPanel.setBackground(new Color(17, 18, 28));
        mainPanel.add(outputPanel);
        new javax.swing.Timer(16, e -> outputPanel.repaint()).start();

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

    static class OutputPanel extends JPanel{
        public static Backend backend;

        public OutputPanel(Backend backend){ OutputPanel.backend = backend;}

        public static float[] sigmoid(float[] input){
            float[] output = new float[input.length];
            for (int i = 0; i < input.length; i++) {
                float x = input[i];
                if (x >= 0){
                    double z = Math.exp(-x);
                    output[i] = (float)(1.0 / (1.0 + z));
                } else{
                    double z = Math.exp(x);
                    output[i] = (float)(z / (1.0 + z));
                }
            }
            return output;
        }

        public static String formatValue(float value) {
            float output;
            if (Math.abs(value) < 1e-6f) {
                output = 0f;
            }
            return String.format("%.3f", value);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Segoe UI Light", Font.PLAIN, 30));
            g.drawString("Model Output", 0, 30);

            float[] outputs = sigmoid(backend.network.outputLayer().activations());

            for (int i = 0; i < 10; i++) {

                g.setColor(Color.WHITE);
                g.setFont(new Font("Segoe UI Light", Font.PLAIN, 25));
                g.drawString(String.valueOf(i),0,(i*30)+70);
                
                g.setColor(new Color(80, 80, 80));
                g.fillRect(30, (i*30)+50, 200,20);
                g.setColor(new Color(96, 195, 90));
                g.fillRect(30, (i*30)+50, (int) (outputs[i]*200),20);

                g.setColor(Color.WHITE);
                g.setFont(new Font("Segoe UI Light", Font.PLAIN, 25));
                g.drawString(formatValue(outputs[i]),50,(i*30)+70);
            }

            g.setColor(Color.WHITE);
            g.setFont(new Font("Segoe UI Light", Font.PLAIN, 30));
            g.drawString("Predicted: ", 0, 380);
            g.setFont(new Font("Segoe UI Light", Font.PLAIN, 80));
            g.drawString(String.valueOf(backend.network.outputLayer().getBrightestNeuronIndex()), 95, 460);
            g.setFont(new Font("Segoe UI Light", Font.PLAIN, 30));
            g.drawString("Confidence: "+ formatValue(outputs[backend.network.outputLayer().getBrightestNeuronIndex()]), 0, 490);
        }
    }
}
