package net.coderodde.circuit.components;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class DualCircuitGate extends CircuitGate {

    protected CircuitGate inputGate1;
    protected CircuitGate inputGate2;
    
    public Set<CircuitGate> getParentGates() {
        return new HashSet<>(Arrays.asList(inputGate1, inputGate2));
    }
    
    public void connectInputGate(CircuitGate inputGate) {
        if (inputGate1 == null) {
            inputGate1 = inputGate;
        } else if (inputGate2 == null) {
            inputGate2 = inputGate;
        } else {
            throw new InputGatesOverflow("This gate has already two inputs.");
        }
        
        inputGate.outputGate = this;
    }
    
    public void disconnectInputGate(CircuitGate inputGate) {
        if (inputGate2 == inputGate) {
            inputGate2.outputGate = null;
            inputGate2 = null;
        } else if (inputGate1 == inputGate) {
            inputGate1.outputGate = null;
            inputGate1 = inputGate2;
        } else {
            throw new IllegalStateException(
                    "The input gate to disconnect is not connected to this " +
                    "gate.");
        }
    }
    
    public void changeInput(CircuitGate inputGate, CircuitGate newInputGate) {
        if (inputGate1 == inputGate) {
            inputGate1 = newInputGate;
        } else {
            inputGate2 = newInputGate;
        }
    }
    
    protected void checkInputGatesPresent() {
        if (inputGate2 == null) {
            throw new NoInputGatesException(
                    "This gate does not have two input gates.");
        }
    }
}
