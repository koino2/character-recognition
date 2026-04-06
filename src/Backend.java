import ai.CytonAI;
import ai.Network;

import java.util.Arrays;

public class Backend {
    public Network network;
    public int inputSize = 32*32;
    public int outputSize = 10;
    public float learningRate = 0.0001f;

    public float[][] trainingSamples;
    public float[][] expectedOutputs;

    public int[] networkSizes = new int[]{inputSize, 256, outputSize};
    public Backend(){
        network = CytonAI.networkBuilder(networkSizes, -1f, 1f);
        network.compute();
    }

    public void train(){
        for (int i = 0; i < trainingSamples.length; i++) {
            network.inputLayer().setActivations(trainingSamples[i]);
            network.compute();
            network.backpropagate(expectedOutputs[i], learningRate, true);
        }
    }

    public void update(PixelCanvas pc){
        network.inputLayer().setActivations(pc.getPixels());
        network.compute();
        System.out.println(Arrays.toString(network.outputLayer().activations()));
        System.out.println(network.outputLayer().getBrightestNeuronIndex());
    }
}
