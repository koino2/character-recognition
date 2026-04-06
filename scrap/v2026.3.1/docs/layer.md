<p align="center">
<img width="50%" height="50%" src="logo.png">
</p>
<h1 align="center">Documentation</h1>

# Layer
### A Layer is a collection of neurons in a neural network.

#### Each Layer contains the following values:
- (Neuron[]) neurons - An array of neurons present in the layer.
## Constructors
### 1. ```public Layer(Neuron[] neurons]```
Creates a new layer whose neurons is the provided neuron[].

### 2. ```public Layer(int length)```
Creeates a new layer where a new neuron[] is assigned for neurons whose length is the provided int length. Every neuron in neurons is initialized with the new Neuron() constructor.

## Methods

### 1. ```setWeightsZero(Layer prevLayer)```
Initializes all the incomingWeights of every neuron in the layer with the neurons from prevLayer (the previous layer) and sets the weight to zero.

### 2. ```setWeightRandom(Layer prevLayer, float min, float max)```
Initializes weights similarly to setWeightsZero, but sets the weight to a random float between min and max.

### 3. ```(Neuron) getBrightestNeuron()```
Returns the neuron whose activation is highest in the layer.

### 4. ```(int) getBrightestNeuronIndex()```
Returns the index of the neuron in neurons whose activation is highest in the layer.

### 5. ```backpropagate(float[] desiredOutput, float learningRate, boolean adjustBias)```
a now useless backpropagate function - use the one in network instead.
Updates the incomingWeights of each neuron to bring the output closer to the activations specified in desiredOutput.  
desiredOutput is the array of desired activations of each neuron.

### 6. ```setActivations(float[] activations)```
Sets the activation of each neuron in the layer to the float in the corresponding index of the activations array.

### 7. ```(float[]) activations()```
Returns a float[]. Each of the floats in the array is the activation of the neuron in neurons of that index.

### 8. ```compute(Layer prevLayer)```
Computes each of the neurons in the layer with prevLayer.