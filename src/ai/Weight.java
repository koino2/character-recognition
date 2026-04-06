package ai;

public class Weight {
    public Neuron prevLayer;
    public Neuron currentLayer;
    public float weight;

    public Weight(Neuron DprevLayer, Neuron DcurrentLayer, float Dweight) {
        prevLayer = DprevLayer;
        currentLayer = DcurrentLayer;
        weight = Dweight;
    }
}