package ai;

import java.util.Random;

public class Layer {
    public Neuron[] neurons;

    public Layer(Neuron[] neurons) {
        this.neurons = neurons;
    }

    public Layer(int length) {
        Neuron[] temp = new Neuron[length];
        for (int i = 0; i < length; i++) {
            temp[i] = new Neuron();
        }
        this.neurons = temp;
    }

    public void setWeightsZero(Layer prevLayer) {
        for (int i = 0; i < neurons.length; i++) {
            neurons[i].incomingWeights = new Weight[prevLayer.neurons.length];
            for (int j = 0; j < neurons[i].incomingWeights.length; j++) {
                neurons[i].incomingWeights[j] = new Weight(prevLayer.neurons[j], neurons[i], 0);
            }
        }
    }

    public void setWeightRandom(Layer prevLayer, float min, float max) {
        for (int i = 0; i < neurons.length; i++) {
            neurons[i].incomingWeights = new Weight[prevLayer.neurons.length];
            for (int j = 0; j < neurons[i].incomingWeights.length; j++) {
                neurons[i].incomingWeights[j] = new Weight(prevLayer.neurons[j], neurons[i], new Random().nextFloat(min, max));
            }
        }
    }

    public Neuron getBrightestNeuron() {
        Neuron brightest = neurons[0];
        for (int i = 0; i < neurons.length; i++) {
            if (neurons[i].activation > brightest.activation) {
                brightest = neurons[i];
            }
        }
        return brightest;
    }

    public int getBrightestNeuronIndex() {
        int brightest = 0;
        for (int i = 0; i < neurons.length; i++) {
            if (neurons[i].activation > neurons[brightest].activation) {
                brightest = i;
            }
        }
        return brightest;
    }

    public void backpropagate(float[] desiredOutput, float learningRate, boolean adjustBias) {
        if (desiredOutput.length != neurons.length) {
            throw new IllegalArgumentException("desiredOutput length must match neuron count");
        }

        for (int i = 0; i < neurons.length; i++) {
            float error = desiredOutput[i] - neurons[i].activation;

            for (int j = 0; j < neurons[i].incomingWeights.length; j++) {
                float gradient = error * neurons[i].incomingWeights[j].prevLayer.activation;
                neurons[i].incomingWeights[j].weight += gradient * learningRate;
            }

            if (adjustBias) {
                neurons[i].bias += error * learningRate;
            }
        }
    }

    public void setActivations(float[] activatons){
        if (activatons.length != neurons.length) {
            throw new IllegalArgumentException("activatons length must match neuron count");
        }

        for (int i = 0; i < neurons.length; i++) {
            neurons[i].activation = activatons[i];
        }
    }
    public float[] activations() {
        float[] activatons = new float[neurons.length];
        for (int i = 0; i < neurons.length; i++) {
            activatons[i] = neurons[i].activation;
        }
        return activatons;
    }

    public void compute(Layer prevlayer) {
        for (int i = 0; i < neurons.length; i++) {
            neurons[i].compute(prevlayer);
        }
    }
}
