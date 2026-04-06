<p align="center">
<img width="50%" height="50%" src="logo.png">
</p>
<h1 align="center">Documentation</h1>

# Network
### A network is a group of layers which form the full model.

#### Each network contains the following values:
- (Layer[]) layers - An array of the layers in the network.

## Constructors
### 1. ```public Network(Layer[] layer)```
Initializes a new network with layers as the provided layer[] layers.

## Methods

### 1. ```compute()```
Computes all the layers except for the input layer using layer.compute(prevlayer)

### 2. ```initWeightsRandom(float min, float max)```
Initialises all the weights in the network as random weights ranging from min to max.

### 3. ```initWeightsZero()```
Initialises all the weights in the network with the weight set to zero.

### 4. ```(Layer) inputLayer()```
Returns the input layer of th enetwork.

### 5. ```(Layer) outputLayer()```
Returns the output layer of the network

### 6. ```backpropagate (float[] expected, float learningRate, boolean changeBias)```
Trains the network using backpropagation.  
expected is the array of activations of neurons which is desired in the network. The function will train the network to bring the results closer to this.  
learningRate is the value which the changes is multiplied by - the learning rate of the training algorithm.  
If changeBias is true, the biases of the neurons will also be changed. If this is not desired, then it should be set to false.