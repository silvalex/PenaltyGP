package wsc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class Sequence extends GPNode {

	@Override
	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack, final GPIndividual individual, final Problem problem) {
		WSCInitializer init = (WSCInitializer) state.initializer;
		double[] qos;
		Set<String> inputs = new HashSet<String>();
		Set<String> outputs = new HashSet<String>();
		int totalInputs = 0;
		int satisfiedInputs = 0;

		WSCData rd = ((WSCData) (input));

		children[0].eval(state, thread, input, stack, individual, problem);
		qos = Arrays.copyOf(rd.qos, rd.qos.length);
		inputs.addAll(rd.inputs);
		outputs.addAll(rd.outputs);
		totalInputs += rd.totalInputs;
		satisfiedInputs += rd.satisfiedInputs;

		children[1].eval(state, thread, input, stack, individual, problem);
		rd.qos[WSCInitializer.TIME] += qos[WSCInitializer.TIME];
		rd.qos[WSCInitializer.COST] += qos[WSCInitializer.COST];
		rd.qos[WSCInitializer.AVAILABILITY] *= qos[WSCInitializer.AVAILABILITY];
		rd.qos[WSCInitializer.RELIABILITY] *= qos[WSCInitializer.RELIABILITY];

		rd.totalInputs += rd.totalInputs; // Add the inputs of the right-hand side
		rd.totalInputs += totalInputs;
		rd.satisfiedInputs += satisfiedInputs;
		rd.satisfiedInputs += init.countInputsSatisfied(outputs, rd.inputs); // We only count the inputs of the right-hand side
		rd.inputs = inputs; // Keep inputs from left child only
		rd.outputs.addAll(outputs); // Outputs from left and right children
	}

	@Override
	public String toString() {
		return "Sequence";
	}

	@Override
	public int expectedChildren() {
		return 2;
	}
}
