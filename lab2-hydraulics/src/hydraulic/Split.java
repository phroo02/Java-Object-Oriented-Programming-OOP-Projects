package hydraulic;

/**
 * Represents a split element, a.k.a. T element
 * 
 * During the simulation each downstream element will
 * receive a stream that is half the input stream of the split.
 */

public class Split extends Element {

	/**
	 * Constructor
	 * @param name name of the split element
	 */
	private Element[] outputs = new Element[2];

	public Split(String name) {
		super(name);
	}
	

	@Override
	public Element[] getOutputs() {
		// return the two downstream elements
		return outputs;
	}
	@Override
	public void connect(Element elem, int index) {
		// connect the first output to the given element
		this.outputs[index] = elem;
		

	}
	
}
