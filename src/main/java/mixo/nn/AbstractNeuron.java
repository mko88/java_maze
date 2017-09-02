package mixo.nn;

import java.util.Random;
import java.util.Vector;

public abstract class AbstractNeuron {
		
	int index;
	double gradient;
	double outputVal;
	Vector<Connection> outputWeights;
	
	static double eta = 0.15;
	static double alpha = 0.5;

	public AbstractNeuron(int numOutputs, int index) {
		this.index = index; 
		outputWeights = new Vector<Connection>();
		for(int c=0; c<numOutputs; ++c) {
			Connection conn = new Connection();
			conn.weight = randomWeight();
			outputWeights.add(conn);
		}				
	}

	public abstract void feedForward(Layer nextLayer);
	public abstract void calcOutputGradients(double targetVal);
	public abstract void calcHiddenGradients(final Layer nextLayer);
	public abstract void updateInputWeights(Layer prevLayer);

	public abstract double sumDOW(Layer nextLayer);
	
	public static double transferFunction(double x) {
		return Math.tanh(x);
	}
	
	public static double transferFunctionDerivative(double x) {
		return 1.0 - x*x;
	}
	
	public static double randomWeight() {
		return 1.0 - new Random().nextFloat();
	}
	
	
	

}
