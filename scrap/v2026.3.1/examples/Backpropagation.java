package examples;

import ai.CytonAI;
import ai.Layer;
import ai.Network;

public class Backpropagation {

    public static void main(String[] args) {

        Network network = CytonAI.networkBuilder(new int[]{3,4,2});
        Layer inputLayer = network.inputLayer();
        Layer outputLayer = network.outputLayer();

        network.initWeightsRandom(-0.5f, 0.5f);

        float[] sampleInput = new float[]{1f, 0f, 0f};
        float[] sampleTarget = new float[]{1f, 0f};

        inputLayer.setActivations(sampleInput);
        network.compute();
        float beforeLoss = CytonAI.mse(outputLayer.activations(), sampleTarget);

        for (int i = 0; i < 10000; i++) {
            inputLayer.setActivations(sampleInput);
            network.compute();
            network.backpropagate(sampleTarget, 0.01f, true);
        }

        inputLayer.setActivations(sampleInput);
        network.compute();
        float[] finalOutput = outputLayer.activations();
        float afterLoss = CytonAI.mse(finalOutput, sampleTarget);

        System.out.println("Target:      " + CytonAI.formatVector(sampleTarget));
        System.out.println("Output:      " + CytonAI.formatVector(finalOutput));
        System.out.printf("Loss before: %.4f%n", beforeLoss);
        System.out.printf("Loss after:  %.4f%n", afterLoss);
        System.out.println();

        if (afterLoss >= beforeLoss) {
            throw new IllegalStateException("Backpropagation test failed: loss did not decrease");
        }else{
            System.out.println("Backpropagation test passed. Loss decreased");
            System.out.println("yay :P");
        }
    }
}
