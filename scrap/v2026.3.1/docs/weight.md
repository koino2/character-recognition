<p align="center">
<img width="50%" height="50%" src="logo.png">
</p>
<h1 align="center">Documentation</h1>

# Weight
### A weight represents the connection between two neurons of different layers.

#### Each weight contains the following values:
- (Neuron) prevLayer
- (Neuron) currentLayer
- (float) weight

## Constructors
### 1. ```public Weight(Neuron DprevLayer, Neuron DcurrentLayer, float Dweight)```
This initializes the values of the weight such that DprevLayer is prevLayer, DcurrentLayer is currentLayer and Dweight is weight.