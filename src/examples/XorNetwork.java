package examples;

import ai.CytonAI;
import ai.Network;
import ai.modules.networkeditor.tools.NetworkEditor;

import java.sql.Time;
import java.util.Arrays;
import java.util.Timer;

public class XorNetwork {

    public static void main(String[] args) {

        // 2 inputs → 2 hidden → 1 output
        int[] structure = {2,2,1};
        Network network = CytonAI.networkBuilder(structure, -1f, 1f);

        float[][] inputs = {
                {0f, 0f},
                {0f, 1f},
                {1f, 0f},
                {1f, 1f}
        };

        float[][] outputs = {
                {0f},
                {1f},
                {1f},
                {0f}
        };

        long start = System.nanoTime();
        // Train the network
        int totalEpochs = 10_000_00;

        for (int epoch = 0; epoch < totalEpochs; epoch++) {

            for (int i = 0; i < inputs.length; i++) {
                network.layers[0].setActivations(inputs[i]);
                network.compute();
                network.backpropagate(outputs[i], 0.05f, true);
            }

            // Update progress every 10k epochs (avoid lag)
            if (epoch % 10_000 == 0) {
                float progress = (epoch / (float) totalEpochs) * 100f;
                System.out.printf("\rTraining: %.2f%%", progress);
            }
        }
        System.out.println("\rTraining complete!");
        long end = System.nanoTime();

        System.out.println("Training time: " + (end - start) / 1000*1000.0 + " ms");
        System.out.println();

        // Test results
        for (int i = 0; i < inputs.length; i++) {
            network.layers[0].setActivations(inputs[i]);
            network.compute();
            System.out.println(
                    inputs[i][0] + " XOR " + inputs[i][1] +
                            " = " + network.layers[network.layers.length-1].activations()[0]

            );

            System.out.println("Loss = " + CytonAI.mse(network.outputLayer().activations(),outputs[i]));
        }

        NetworkEditor.openWindow(network);
    }

}
