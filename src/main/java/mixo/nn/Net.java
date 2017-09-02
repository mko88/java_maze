package mixo.nn;

import java.util.Vector;

public class Net extends AbstractNet {

	public Net(Vector<Integer> topology) {
		super(topology);
	}

	@Override
	public void feedForward(Vector<Double> inputVals) {
		if(inputVals.size() != layers.get(0).size() -1) {
			throw new RuntimeException("input values size not equal neuron size");
		}
		
		for(int i=0; i<inputVals.size(); i++) {
			layers.get(0).get(i).outputVal = inputVals.get(i);
		}
		
		for(int layerNum = 1; layerNum < layers.size(); layerNum++) {
			Layer prevLayer = layers.get(layerNum - 1);
			for(int n=0; n<layers.get(layerNum).size() - 1; ++n) {
				layers.get(layerNum).get(n).feedForward(prevLayer);
			}
		}
	}

	@Override
	public void backProp(Vector<Double> targetVals) {
		Layer outputLayer = layers.lastElement();
		//error = 0.0;
		for(int n=0; n<outputLayer.size() - 1; ++n) {
			AbstractNeuron neuron = outputLayer.get(n);
			double delta = targetVals.get(n) - neuron.outputVal; 
			error += delta*delta;
		}
		
		error /= outputLayer.size()-1;
		error = Math.sqrt(error);
		
		recentAvarageError = (recentAvarageError * recentAvarageSmoothingFactor + error)
				/ (recentAvarageSmoothingFactor + 1.0);
		
		for(int n=0; n<outputLayer.size() - 1; ++n) {
			AbstractNeuron neuron = outputLayer.get(n);
			neuron.calcOutputGradients(targetVals.get(n));
		}
		
		for(int layerNum=layers.size() - 2; layerNum>0; --layerNum) {
			Layer hiddenLayer = layers.get(layerNum);
			Layer nextLayer = layers.get(layerNum + 1);
			for(int n=0; n<hiddenLayer.size(); ++n) {
				hiddenLayer.get(n).calcHiddenGradients(nextLayer);
			}
		}
		
		for(int layerNum = layers.size()-1; layerNum>0; --layerNum) {
			Layer layer = layers.get(layerNum);
			Layer prevLayer = layers.get(layerNum - 1);
			
			for(int n=0; n<layer.size() - 1; ++n) {
				layer.get(n).updateInputWeights(prevLayer);
			}
		}
	}

	@Override
	public void getResult(Vector<Double> resultVals) {
		resultVals.clear();
		for(int n=0;n<layers.lastElement().size();n++) {
			resultVals.add(layers.lastElement().get(n).outputVal);
		}
	}

}
