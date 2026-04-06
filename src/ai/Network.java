package ai;

public class Network {
    public Layer[] layers;

    public Network(Layer[] layer) {
        this.layers = layer;
    }

    public void compute() {
        for (int i = 0; i < layers.length; i++) {
            if (i != 0) {
                layers[i].compute(layers[i - 1]);
            }
        }
    }

    public void initWeightsRandom(float min, float max){
        for (int i = 1; i < layers.length; i++) {
            layers[i].setWeightRandom(layers[i-1],min,max);
        }
    }

    public void initWeightsZero(){
        for (int i = 1; i < layers.length; i++) {
            layers[i].setWeightsZero(layers[i-1]);
        }
    }

    public Layer inputLayer(){
        return layers[0];
    }

    public Layer outputLayer(){
        return layers[layers.length-1];
    }

    // experimental ai slop cuz i dont understand anything :(
    public void backpropagate(float[] expected, float learningRate, boolean changeBias) {
        if (layers.length < 2) {
            return;
        }
        if (expected.length != outputLayer().neurons.length) {
            throw new IllegalArgumentException("expected length must match output layer size");
        }

        float[][] deltas = new float[layers.length][];

        int outputLayerIndex = layers.length - 1;
        Layer output = layers[outputLayerIndex];
        deltas[outputLayerIndex] = new float[output.neurons.length];
        for (int i = 0; i < output.neurons.length; i++) {
            // Activation function is linear, so derivative = 1.
            deltas[outputLayerIndex][i] = expected[i] - output.neurons[i].activation;
        }

        // Propagate error backwards through hidden layers (skip input layer at index 0)
        for (int layerIndex = outputLayerIndex - 1; layerIndex >= 1; layerIndex--) {
            Layer layer = layers[layerIndex];
            Layer next = layers[layerIndex + 1];
            deltas[layerIndex] = new float[layer.neurons.length];

            for (int i = 0; i < layer.neurons.length; i++) {
                float weightedError = 0;
                for (int j = 0; j < next.neurons.length; j++) {
                    weightedError += next.neurons[j].incomingWeights[i].weight * deltas[layerIndex + 1][j];
                }
                // Activation function is linear, so derivative = 1.
                deltas[layerIndex][i] = weightedError;
            }
        }

        // Update all trainable layers (skip input layer)
        for (int layerIndex = 1; layerIndex < layers.length; layerIndex++) {
            Layer current = layers[layerIndex];
            Layer previous = layers[layerIndex - 1];

            for (int neuronIndex = 0; neuronIndex < current.neurons.length; neuronIndex++) {
                float delta = deltas[layerIndex][neuronIndex];

                for (int weightIndex = 0; weightIndex < current.neurons[neuronIndex].incomingWeights.length; weightIndex++) {
                    float gradient = delta * previous.neurons[weightIndex].activation;
                    current.neurons[neuronIndex].incomingWeights[weightIndex].weight += learningRate * gradient;
                }

                if (changeBias) {
                    current.neurons[neuronIndex].bias += learningRate * delta;
                }
            }
        }
    }
}
