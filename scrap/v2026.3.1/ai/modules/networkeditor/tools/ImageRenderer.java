package ai.modules.networkeditor.tools;

import ai.Layer;
import ai.Network;
import ai.Neuron;
import ai.Weight;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

public class ImageRenderer {

    public static int getMaxNeuronsLength(Layer[] layers) {
        int maxNeuronsLength = 0;
        for (Layer layer : layers) {
            if (layer.neurons.length > maxNeuronsLength) {
                maxNeuronsLength = layer.neurons.length;
            }
        }
        return maxNeuronsLength;
    }

    public static float round(float value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Float.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    public static BufferedImage renderImage(Network n) {

        Layer[] layers = n.layers;

        int width = (layers.length * 175) + 800;
        int height = (getMaxNeuronsLength(layers) * 100) + 800;

        BufferedImage neuron;
        try {
            neuron = ImageIO.read(new File("src/ai/modules/networkeditor/resources/neuron.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = (Graphics2D) temp.getGraphics();

        g.setColor(new Color(22, 24, 29));
        g.fillRect(0, 0, width, height);

        for (int i = 0; i < layers.length; i++) {
            int yStart = (height - (layers[i].neurons.length * neuron.getHeight())) / 2;
            for (int j = 0; j < layers[i].neurons.length; j++) {
                int x = i * neuron.getWidth() * 2;
                int y = yStart + j * neuron.getHeight();
                g.drawImage(neuron, x + 350, y, null);
                g.setFont(new Font("Segoe UI", Font.PLAIN, 42));
                g.setColor(Color.WHITE);
                float roundedValue = round(layers[i].neurons[j].activation, 2);
                g.drawString(String.valueOf(roundedValue), x + 372, y + 65);

                for (int k = 0; k < layers[i].neurons[j].incomingWeights.length; k++) {
                    Weight weight = layers[i].neurons[j].incomingWeights[k];
                    Neuron prevNeuron = weight.prevLayer;
                    if (i > 0) {
                        int prevLayerIndex = i - 1;
                        int prevLayerYStart = (height - (layers[prevLayerIndex].neurons.length * neuron.getHeight())) / 2;
                        int prevNeuronIndex = Arrays.asList(layers[prevLayerIndex].neurons).indexOf(prevNeuron);
                        if (prevNeuronIndex != -1) {
                            int prevX = prevLayerIndex * neuron.getWidth() * 2 + 350;
                            int prevY = prevLayerYStart + prevNeuronIndex * neuron.getHeight() + neuron.getHeight() / 2;

                            if (weight.weight > 0) {
                                g.setColor(new Color(116, 255, 91));
                            } else if (weight.weight < 0) {
                                g.setColor(new Color(255, 50, 50));
                            }

                            float thickness = weight.weight;
                            if (thickness < 0) {
                                thickness = thickness * -1;
                            }

                            g.setStroke(new BasicStroke(thickness));

                            if(weight.weight != 0 ) {
                                g.drawLine(prevX + neuron.getWidth(), prevY, x + 350, y + neuron.getHeight() / 2);
                            }
                        }
                    }
                }
            }
        }

        return temp;
    }

    public static BufferedImage renderScaled (int width, int height, Network n) {
            BufferedImage image = ImageRenderer.renderImage(n);
            BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            if (image != null) {
                Graphics2D g = (Graphics2D) temp.getGraphics();
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                int panelWidth = width;
                int panelHeight = height;
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

                g.setColor(Color.BLACK);
                g.fillRect(0, 0, width, height);
                g.drawImage(image, x, y, newWidth, newHeight, null);

                g.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                g.setColor(Color.WHITE);
            }
            return temp;
        }
}