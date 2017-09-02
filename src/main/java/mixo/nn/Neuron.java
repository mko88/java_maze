package mixo.nn;

public class Neuron extends AbstractNeuron{	
	
	public Neuron(int numOutputs, int index) {
		super(numOutputs, index);
		
	}

	@Override
	public void feedForward(Layer prevLayer) {
		double sum = 0.0;
		for(int n=0;n<prevLayer.size(); ++n) {
			AbstractNeuron neuron = prevLayer.get(n);
			sum+=neuron.outputVal * 
					neuron.outputWeights.get(index).weight;
		}		
		outputVal = transferFunction(sum);
		
	}

	@Override
	public void calcOutputGradients(double targetVal) {
		double delta = targetVal - outputVal;
		gradient = delta * transferFunctionDerivative(outputVal);
	}

	@Override
	public void calcHiddenGradients(Layer nextLayer) {
		double dow = sumDOW(nextLayer);
		gradient = dow * transferFunctionDerivative(outputVal);
	}

	@Override
	public void updateInputWeights(Layer prevLayer) {
		for(int n=0; n<prevLayer.size(); ++n) {
			AbstractNeuron neuron = prevLayer.get(n);
			double oldDeltaWeight = neuron.outputWeights.get(index).deltaWeight;
			
			double newDeltaWeight = 
					eta
					*neuron.outputVal
					*gradient
					+alpha
					*oldDeltaWeight;
			neuron.outputWeights.get(index).deltaWeight = newDeltaWeight;
			neuron.outputWeights.get(index).weight += newDeltaWeight;
		}
	}

	@Override
	public double sumDOW(Layer nextLayer) {
		double sum = 0.0;
		for(int n=0; n<nextLayer.size() - 1; ++n) {
			sum+=outputWeights.get(n).weight * nextLayer.get(n).gradient;
		}
		return sum;
	}

}
