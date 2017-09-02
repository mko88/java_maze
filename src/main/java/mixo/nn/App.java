package mixo.nn;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class App {

	public static void main(String[] args) throws InterruptedException {
		Vector<Integer> topology = new Vector<Integer>();
		topology.add(3);
		topology.add(8);
		topology.add(2);
		int pass = 0;
		Net net = new Net(topology);
		
		Random rand = new Random();		
		
		List<Double[]> inputs = new ArrayList<Double[]>();		
		List<Double[]> targetOutputs = new ArrayList<Double[]>();
		
		inputs.add(new Double[] {0.0, 0.0, 0.0});
		targetOutputs.add(new Double[] {0.0, 0.0});
		
		inputs.add(new Double[] {0.0, 0.0, 1.0});
		targetOutputs.add(new Double[] {0.0, 1.0});
		
		inputs.add(new Double[] {0.0, 1.0, 0.0});
		targetOutputs.add(new Double[] {0.0, 1.0});
		
		inputs.add(new Double[] {0.0, 1.0, 1.0});
		targetOutputs.add(new Double[] {1.0, 0.0});
		
		inputs.add(new Double[] {1.0, 0.0, 0.0});
		targetOutputs.add(new Double[] {0.0, 1.0});
		
		inputs.add(new Double[] {1.0, 0.0, 1.0});
		targetOutputs.add(new Double[] {1.0, 0.0});
		
		inputs.add(new Double[] {1.0, 1.0, 0.0});
		targetOutputs.add(new Double[] {1.0, 0.0});
		
		inputs.add(new Double[] {1.0, 1.0, 1.0});
		targetOutputs.add(new Double[] {1.0, 1.0});
		
		test(net, 0.0, 0.0, 0.0);
		test(net, 0.0, 1.0, 0.0);
		test(net, 1.0, 0.0, 0.0);
		test(net, 1.0, 1.0, 0.0);
		while(pass<2000) {
			pass++;			
			int nextIdx = rand.nextInt(inputs.size());			
			Vector<Double> inputVals = new Vector<Double>();
			inputVals.add(inputs.get(nextIdx)[0]);
			inputVals.add(inputs.get(nextIdx)[1]);
			inputVals.add(inputs.get(nextIdx)[2]);
			net.feedForward(inputVals);
			Vector<Double> resultVals = new Vector<Double>();
			net.getResult(resultVals);
			if(pass<=100) {
				System.out.printf("[%5d] Input: %s Output: [%5f][%5f]\n",pass, inputVals, 
						resultVals.get(0), resultVals.get(1));
			}
			
			Vector<Double> targetVals = new Vector<Double>();
			targetVals.add(targetOutputs.get(nextIdx)[0]);
			targetVals.add(targetOutputs.get(nextIdx)[1]);
			//targetVals.add(targetOutputs.get(nextIdx)[2]);
			net.backProp(targetVals);
			//Thread.sleep(1000);
		}
		System.out.println("------------------------------");	
		test(net, 0.0, 0.0, 0.0);
		test(net, 0.0, 1.0, 0.0);
		test(net, 0.0, 1.0, 1.0);
		test(net, 1.0, 0.0, 0.0);
		test(net, 1.0, 1.0, 0.0);		
		test(net, 1.0, 0.0, 1.0);
		test(net, 1.0, 1.0, 1.0);
				
	}
	
	private static void test(Net net, double i1, double i2, double i3) {
		Vector<Double> inputVals = new Vector<Double>();
		inputVals.add(i1);
		inputVals.add(i2);
		inputVals.add(i3);
		net.feedForward(inputVals);
		Vector<Double> resultVals = new Vector<Double>();
		net.getResult(resultVals);
		Double d1 = resultVals.get(0);
		Double d2 = resultVals.get(1);
		String o1 = "NAN" + d1;
		String o2 = "NAN" + d2;
		if(d1 >= 0.85) {
			o1 = "1";
		}
		if(d1 <= 0.15) {
			o1 = "0";
		}
		if(resultVals.get(1) >= 0.85) {
			o2 = "1";
		}
		if(resultVals.get(1) <= 0.15) {
			o2 = "0";
		}		
		System.out.printf("---------------------------------------------------\n");		
		System.out.printf(" Input: %s Output: [%3s][%3s]\n",inputVals, o1, o2);
		System.out.printf(" Input: %s Output: [%5f][%5f]\n",inputVals, d1, d2);
	}

}
