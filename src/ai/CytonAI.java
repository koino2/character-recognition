package ai;

public class CytonAI {

    /*

    This class contains a bunch of tools that wouldnt fit anywhere else
    and are too cool to be in a module :D

    TODO: update the version every 30 seconds lol

     */

    public static String version = "2026.3";

    //network builder does the annoying stuff for u :)
    public static Network networkBuilder(int[] layerSizes, float initMin, float initMax){
        Layer[] layers = new Layer[layerSizes.length];
        for (int i = 0; i < layerSizes.length; i++) {
            layers[i] = new Layer(layerSizes[i]);
        }
        Network network = new Network(layers);
        network.initWeightsRandom(initMin, initMax);
        return network;
    }
    public static Network networkBuilder(int[] layerSizes){
        Layer[] layers = new Layer[layerSizes.length];
        for (int i = 0; i < layerSizes.length; i++) {
            layers[i] = new Layer(layerSizes[i]);
        }
        Network network = new Network(layers);
        network.initWeightsZero();
        return network;
    }

    public static float mse(float[] predicted, float[] target) { //mean squared error
        float sum = 0;
        for (int i = 0; i < predicted.length; i++) {
            float d = target[i] - predicted[i];
            sum += d * d;
        }
        return sum / predicted.length;
    }

    public static String formatVector(float[] values) {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < values.length; i++) {
            float value = values[i];
            if (Math.abs(value) < 1e-6f) {
                value = 0f;
            }
            builder.append(String.format("%.6f", value));
            if (i < values.length - 1) {
                builder.append(", ");
            }
        }
        builder.append("]");
        return builder.toString();
    }
}
