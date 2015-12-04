package wsc;

import java.util.Arrays;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import ec.util.MersenneTwisterFast;

public class ServiceNode extends GPNode {
	private static final long serialVersionUID = 1L;
	private Service service;

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack, final GPIndividual individual, final Problem problem) {
		if (service == null) {
			MersenneTwisterFast m = state.random[thread];
			service = ((WSCInitializer)state.initializer).getRandomService(m);
		}

		WSCData rd = ((WSCData) (input));
		rd.qos = Arrays.copyOf(service.qos, service.qos.length);
		rd.inputs.addAll(service.inputs);
		rd.outputs.addAll(service.outputs);
		rd.totalInputs = service.inputs.size();
		rd.satisfiedInputs = service.inputs.size();
	}

	@Override
	public String toString() {
		if (service == null)
			return "null";
		else
			return service.name;
	}

	@Override
	public int expectedChildren() {
		return 0;
	}

	@Override
	public int hashCode() {
		return service.name.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof ServiceNode) {
			ServiceNode o = (ServiceNode) other;
			return service.name.equals(o.service.name);
		}
		else
			return false;
	}
}
