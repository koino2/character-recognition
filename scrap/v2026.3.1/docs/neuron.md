<p align="center">
<img width="50%" height="50%" src="logo.png">
</p>
<h1 align="center">Documentation</h1>

# Neuron
#### Each neuron contains the following values:
- (float) activation - The activation of the neuron
- (float) bias - The bias of the neuron.
- (Weight[]) incomingWeights - An array of weights. These are supposed to be the weights connecting all the neurons of the previous layer to this particular neuron.
- (float[]) incomingActivations - This is the array of activations of the neurons from the previous layer.

Note: The indexes of the incomingWeights and incomingActivations should refer to the same neuron.

## Constructors
### 1. ```public Neuron(float activationV, biasV)```  
This creates a new neuron whose activation is activationV and bias is biasV.  
Here, incomingWeights and incomingActivations are not assigned.

### 2. ```public Neuron(float activationV, float biasV, Weight[] weights)```
This creates a new neuron whose activation is activationV, bias is biasV and incomingWeights is the provided Weight[] weights.  
Here, incomingActivations is not assigned.

### 3. ```public Neuron()```
This creates a new neuron, where activation is 0, bias is 0, and incomingWeights is made as a temporary Weight[] of 1 Weight for initialisation purpose.

## Methods

### 1. ```compute()```
Takes no arguments. It computes the neurons activation.  
The activation of the neuron is computed with the following code.  
For this to work, all the 4 variables must be properly assigned.
```
float temp = 0;
for (int i = 0; i < incomingActivations.length; i++) {
    temp += incomingActivations[i] * incomingWeights[i].weight;
}
activation = (temp);
activation += bias;
```

### 2. ```compute(Layer prevLayer)```
Takes one argument, which is the layer which comes before the layer in which this neuron is in.  
Updates the activation similarly to how compute() does it.  
Here, all the variables must be assigned except for incomingActivations, as it is taken from the activations of prevLayer.