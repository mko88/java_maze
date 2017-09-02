package mixo.nn;

import java.util.Vector;

public abstract class AbstractNet {

	Vector<Layer> layers;	
	double error;
	double recentAvarageError;
	double recentAvarageSmoothingFactor;
	
	public AbstractNet(Vector<Integer> topology) {
		layers = new Vector<Layer>();
		int numLayers = topology.size();
		for(int layerNum=0; layerNum<numLayers; ++layerNum) {
			Layer layer = new Layer();
			layers.addElement(layer);
			int numOutputs = layerNum == numLayers - 1 ? 0 : topology.get(layerNum + 1);
			
			for(int neuronNum=0; neuronNum<=topology.get(layerNum); ++neuronNum) {
				layer.add(new Neuron(numOutputs, neuronNum));
			}
			layers.lastElement().lastElement().outputVal = 1.0;
		}		
		//bias
		
	}
	
	public abstract void feedForward(Vector<Double> inputVals);
	public abstract void backProp(Vector<Double> targetVals);
	public abstract void getResult(Vector<Double> resultVals);
	
}
