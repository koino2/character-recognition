<p align="center">
<img width="50%" height="50%" src="logo.png">
</p>
<h1 align="center">Documentation</h1>

# The Cyton AI Class
### The CytonAI class is a class where random (mostly static) utilities or tools that dont fit in anywhere else go.

#### It contains the following values:
- (static String) version - The current version of the CytonAI installation <i>if i remember to update it</i>

## NetworkBuilder
> NetworkBuilder does the annoying stuff of building and initializing the network for you.
### 1. ```networkBuilder(int[] layerSizes, float initMin, float initMax)```
Returns a ready to use network, initialized with random weights between initMin and initMax.

### 2. ```networkBuilder(int[] layerSizes)```
Returns a ready to use network, initialized with all weights as zero.

## Utils
> Random stuff that might be useful
### 1. ```mse(float[] predicted, float[] target)```
Mean squared error.  
predicted -> what the model gave  
target -> what you want

### 2. ```formatVector(float[] values)```
basically cleans up the ugly numbers and like rounds them up because the gui crashes out a lot.

<!-- DOCUMENTATION OVERR
FINALLY FREEEE
-->