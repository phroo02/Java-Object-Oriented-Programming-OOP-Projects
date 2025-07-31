package hydraulic;

/**
 * Hydraulics system builder providing a fluent API
 */
public class HBuilder {

    private HSystem system = new HSystem();
    private Element lastElement = null;
    private Element currentElement = null;
    private Element multiOutputElement = null;
    private int outputIndex = 0;
    private boolean inMultiOutputMode = false;
    private boolean justEnteredMultiOutput = false;
    private java.util.Deque<Element> multiOutputStack = new java.util.ArrayDeque<>();
    private java.util.Deque<Integer> outputIndexStack = new java.util.ArrayDeque<>();

    public HBuilder addSource(String name) {
        Source src = new Source(name);
        system.addElement(src);
        currentElement = src;
        lastElement = src;
        return this;
    }

    public HSystem complete() {
        return system;
    }

    public HBuilder linkToTap(String name) {
        Tap tap = new Tap(name);
        system.addElement(tap);
        connectToPrevious(tap);
        currentElement = tap;
        return this;
    }

    public HBuilder linkToSink(String name) {
        Sink sink = new Sink(name);
        system.addElement(sink);
        connectToPrevious(sink);
        currentElement = sink;
        return this;
    }

    public HBuilder linkToSplit(String name) {
        Split split = new Split(name);
        system.addElement(split);
        connectToPrevious(split);
        currentElement = split;
        multiOutputElement = split;
        return this;
    }

    public HBuilder linkToMultisplit(String name, int numOutput) {
        Multisplit ms = new Multisplit(name, numOutput);
        system.addElement(ms);
        connectToPrevious(ms);
        currentElement = ms;
        multiOutputElement = ms;
        return this;
    }

    public HBuilder withOutputs() {
        multiOutputStack.push(multiOutputElement);
        outputIndexStack.push(outputIndex);

        lastElement = multiOutputElement;
        outputIndex = 0;
        inMultiOutputMode = true;
        justEnteredMultiOutput = true;
        return this;
    }

    public HBuilder then() {
        outputIndex++;
        lastElement = multiOutputElement;
        justEnteredMultiOutput = true;
        return this;
    }

    public HBuilder done() {
        multiOutputElement = multiOutputStack.pop();
        outputIndex = outputIndexStack.pop();

        if (multiOutputStack.isEmpty()) {
            multiOutputElement = null;
            outputIndex = 0;
            inMultiOutputMode = false;
            justEnteredMultiOutput = false;
        } else {
            multiOutputElement = multiOutputStack.peek();
            outputIndex = outputIndexStack.peek();
        }
        return this;
    }

    public HBuilder withFlow(double flow) {
        if (currentElement instanceof Source) {
            ((Source) currentElement).setFlow(flow);
        }
        return this;
    }

    public HBuilder open() {
        if (currentElement instanceof Tap) {
            ((Tap) currentElement).setOpen(true);
        }
        return this;
    }

    public HBuilder closed() {
        if (currentElement instanceof Tap) {
            ((Tap) currentElement).setOpen(false);
        }
        return this;
    }

    public HBuilder withPropotions(double[] props) {
        if (currentElement instanceof Multisplit) {
            ((Multisplit) currentElement).setProportions(props);
        }
        return this;
    }

    public HBuilder maxFlow(double max) {
        if (currentElement != null) {
            currentElement.setMaxFlow(max);
        }
        return this;
    }

    private void connectToPrevious(Element newElement) {
        if (inMultiOutputMode && multiOutputElement != null && justEnteredMultiOutput) {
            multiOutputElement.connect(newElement, outputIndex);
            justEnteredMultiOutput = false;
        } else {
            lastElement.connect(newElement);
        }
        currentElement = newElement;
        lastElement = newElement;
    }
} 
