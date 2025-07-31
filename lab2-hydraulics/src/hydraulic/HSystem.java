package hydraulic;

/**
 * Main class that acts as a container of the elements for
 * the simulation of an hydraulics system 
 * 
 */
public class HSystem {

// R1
	/**
	 * Adds a new element to the system
	 * 
	 * @param elem the new element to be added to the system
	 */
	Element [] elements = new Element[100];
	int elementCount = 0;

	public void addElement(Element elem){
		elements[elementCount] = elem;
		elementCount++;
	}

	/**
	 * returns the number of element currently present in the system
	 * 
	 * @return count of elements
	 */
	public int size() {
		return elementCount;
    }

	/**
	 * returns the element added so far to the system
	 * 
	 * @return an array of elements whose length is equal to 
	 * 							the number of added elements
	 */
	public Element[] getElements(){
		
		Element[] elementsCopy = new Element[elementCount];
		for (int i = 0; i < elementCount; i++) {
			elementsCopy[i] = elements[i];
		}
		return elementsCopy;
    }
	

// R4
	/**
	 * starts the simulation of the system
	 * 
	 * The notification about the simulations are sent
	 * to an observer object
	 * 
	 * Before starting simulation the parameters of the
	 * elements of the system must be defined
	 * 
	 * @param observer the observer receiving notifications
	 */
	public void simulate(SimulationObserver observer){
		for (Element e : elements) {
			if (e instanceof Source) {
			    simulateElement(e, ((Source)e).getFlow(), observer);
			}
		   }
		   
	}
	private void simulateElement(Element e, double inFlow, SimulationObserver obs) {
		if (e instanceof Source) {
			
		    Source s = (Source)e;
		    double outFlow = s.getFlow(); // define this method in Source
		    obs.notifyFlow("Source", s.getName(), SimulationObserver.NO_FLOW, outFlow);
		    simulateElement(s.getOutput(), outFlow, obs);

		} else if (e instanceof Tap) {

		    Tap t = (Tap)e;
		    double outFlow = t.isOpen() ? inFlow : 0.0;
		    obs.notifyFlow("Tap", t.getName(), inFlow, outFlow);
		    simulateElement(t.getOutput(), outFlow, obs);

		} else if (e instanceof Split) {
			if (e instanceof Multisplit) {
				Multisplit m = (Multisplit) e;
				Element[] outs = m.getOutputs();
				double[] props = m.getProportions(); // you'll need a getter
				double[] outFlows = new double[outs.length];
			   
				for (int i = 0; i < outs.length; i++) {
				    outFlows[i] = inFlow * props[i];
				}
			   
				obs.notifyFlow("Multisplit", m.getName(), inFlow, outFlows);
			   
				for (int i = 0; i < outs.length; i++) {
				    if (outs[i] != null) simulateElement(outs[i], outFlows[i], obs);
				}
			}else {

		    		Split s = (Split)e;
				double [] out = {inFlow / 2.0, inFlow / 2.0};
				

				obs.notifyFlow("Split", s.getName(), inFlow, out);
				Element[] outs = s.getOutputs();
				if (outs[0] != null) simulateElement(outs[0], out[0], obs);
				if (outs[1] != null) simulateElement(outs[1], out[1], obs);
			}

		} else if (e instanceof Sink) {

		    obs.notifyFlow("Sink", e.getName(), inFlow, SimulationObserver.NO_FLOW);


		}
	   }
	   


// R6
	/**
	 * Deletes a previously added element 
	 * with the given name from the system
	 */
	public boolean deleteElement(String name) {
		// Step 1: Find the element to delete and its index
		Element toDelete = null;
		int index = -1;
	   
		for (int i = 0; i < elementCount; i++) {
		    if (elements[i].getName().equals(name)) {
			 toDelete = elements[i];
			 index = i;
			 break;
		    }
		}
	   
		if (toDelete == null) return false; // Not found
	   
		// Step 2: If it's a Split or Multisplit, check connected outputs
		if (toDelete instanceof Split || toDelete instanceof Multisplit) {
		    Element[] outs = toDelete.getOutputs();
		    int connectedCount = 0;
		    if (outs != null) {
			 for (Element e : outs) {
			     if (e != null) connectedCount++;
			 }
		    }
		    if (connectedCount > 1) return false;
		}
	   
		// Step 3: Find upstream element and output index
		Element upstream = null;
		int outputIndex = -1;
	   
		for (Element e : elements) {
		    if (e == null || e == toDelete) continue;
	   
		    Element[] outs = e.getOutputs();
		    if (outs != null) { // for Split and Multisplit
			 for (int i = 0; i < outs.length; i++) {
			     if (outs[i] == toDelete) {
				  upstream = e;
				  outputIndex = i;
				  break;
			     }
			 }
		    } else if (e.getOutput() == toDelete) { // for single output elements
			 upstream = e;
			 outputIndex = 0;
		    }
	   
		    if (upstream != null) break;
		}
	   
		// Step 4: Find the downstream element connected to `toDelete`
		Element downstream = null;
		Element[] toDeleteOuts = toDelete.getOutputs();
		if (toDeleteOuts != null) {
		    for (Element out : toDeleteOuts) {
			 if (out != null) {
			     downstream = out;
			     break;
			 }
		    }
		} else {
		    downstream = toDelete.getOutput();
		}
	   
		// Step 5: Reconnect upstream to downstream
		if (upstream != null) { // anything except deleting the source
			if (upstream instanceof Split || upstream instanceof Multisplit) {
			    upstream.connect(downstream, outputIndex);
			} else {
			    upstream.connect(downstream);
			}
		}
	   
		// Step 6: Disconnect downstreams from the toDelete element (cleanup)
		if (toDeleteOuts != null) {
		    for (int i = 0; i < toDeleteOuts.length; i++) {
			 toDeleteOuts[i] = null;
		    }
		}
	   
		// Step 7: Remove element from array and shift left
		for (int i = index; i < elementCount - 1; i++) {
		    elements[i] = elements[i + 1];
		}
		elements[--elementCount] = null;
	   
		return true;
	}
	   
	   
// R7
	/**
	 * starts the simulation of the system; if {@code enableMaxFlowCheck} is {@code true},
	 * checks also the elements maximum flows against the input flow
	 * 
	 * If {@code enableMaxFlowCheck} is {@code false}  a normals simulation as
	 * the method {@link #simulate(SimulationObserver)} is performed
	 * 
	 * Before performing a checked simulation the max flows of the elements in thes
	 * system must be defined.
	 */
	public void simulate(SimulationObserver observer, boolean enableMaxFlowCheck) {
		for (Element e : elements) {
			if (e instanceof Source) {
			    Source s = (Source) e;
			    simulateElement(s, s.getFlow(), observer, enableMaxFlowCheck);
			}
		}
	}
	private void simulateElement(Element e, double inFlow, SimulationObserver obs,boolean enableMaxFlowCheck) {
		if (e instanceof Source) {
			
		    Source s = (Source)e;
		    
		    double outFlow = s.getFlow(); // define this method in Source
		    obs.notifyFlow("Source", s.getName(), SimulationObserver.NO_FLOW, outFlow);
		    simulateElement(s.getOutput(), outFlow, obs, enableMaxFlowCheck);

		} else if (e instanceof Tap) {

		    Tap t = (Tap)e;
		    if (enableMaxFlowCheck && inFlow > t.getMaxFlow()) {
			obs.notifyFlowError(t.getClass().getSimpleName(), t.getName(), inFlow, t.getMaxFlow());
		   }
		   
		    double outFlow = t.isOpen() ? inFlow : 0.0;
		    obs.notifyFlow("Tap", t.getName(), inFlow, outFlow);
		    simulateElement(t.getOutput(), outFlow, obs, enableMaxFlowCheck);

		} else if (e instanceof Split) {
			if (e instanceof Multisplit) {
				Multisplit m = (Multisplit) e;
				if (enableMaxFlowCheck && inFlow > m.getMaxFlow()) {
					obs.notifyFlowError("Multisplit", m.getName(), inFlow, m.getMaxFlow());
				}
				Element[] outs = m.getOutputs();
				double[] props = m.getProportions(); // you'll need a getter
				double[] outFlows = new double[outs.length];
			   
				for (int i = 0; i < outs.length; i++) {
				    outFlows[i] = inFlow * props[i];
				}
			   
				obs.notifyFlow("Multisplit", m.getName(), inFlow, outFlows);
			   
				for (int i = 0; i < outs.length; i++) {
				    if (outs[i] != null) simulateElement(outs[i], outFlows[i], obs, enableMaxFlowCheck);
				}
			}else {

		    		Split s = (Split)e;
				if (enableMaxFlowCheck && inFlow > s.getMaxFlow()) {
					obs.notifyFlowError("Split", s.getName(), inFlow, s.getMaxFlow());
				}
				double [] out = {inFlow / 2.0, inFlow / 2.0};
				

				obs.notifyFlow("Split", s.getName(), inFlow, out);
				Element[] outs = s.getOutputs();
				if (outs[0] != null) simulateElement(outs[0], out[0], obs, enableMaxFlowCheck);
				if (outs[1] != null) simulateElement(outs[1], out[1], obs,enableMaxFlowCheck);
			}

		} else if (e instanceof Sink) {
			Sink si = (Sink)e;
			if (enableMaxFlowCheck && inFlow > si.getMaxFlow()) {
				obs.notifyFlowError(si.getClass().getSimpleName(), si.getName(), inFlow, si.getMaxFlow());
			}
		    obs.notifyFlow("Sink", e.getName(), inFlow, SimulationObserver.NO_FLOW);


		}
	   }


// R8
	/**
	 * creates a new builder that can be used to create a 
	 * hydraulic system through a fluent API 
	 * 
	 * @return the builder object
	 */
    public static HBuilder build() {
		
		
		return new HBuilder();
    }
}
