package ai;

public class Neuron {
    public float activation;
    public float bias;
    public Weight[] incomingWeights;
    public float[] incomingActivations;

    public Neuron(float activationV, float biasV) {
        activation = activationV;
        bias = biasV;
    }

    public Neuron(float activationV, float biasV, Weight[] weights) {
        activation = activationV;
        bias = biasV;
        incomingWeights = weights;
    }

    public Neuron() {
        activation = 0;
        bias = 0;
        Weight[] yo = new Weight[1];
        yo[0] = new Weight(this, this, 0);
        incomingWeights = yo;
    }

    public void compute() {
        float temp = 0;
        for (int i = 0; i < incomingActivations.length; i++) {
            temp += incomingActivations[i] * incomingWeights[i].weight;
        }
        activation = (temp);
        activation += bias;
    }

    public void compute(Layer prevLayer) {
        float temp = 0;
        for (int i = 0; i < prevLayer.neurons.length; i++) {
            temp += prevLayer.neurons[i].activation * incomingWeights[i].weight;
        }
        activation = (temp);
        activation += bias;
    }
}