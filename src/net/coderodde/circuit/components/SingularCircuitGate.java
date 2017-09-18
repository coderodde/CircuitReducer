package net.coderodde.circuit.components;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class SingularCircuitGate extends CircuitGate {

    protected CircuitGate inputGate;
    
    public void connectInputGate(CircuitGate inputGate) {
        this.inputGate = Objects.requireNonNull(inputGate, 
                                                "The input gate is null.");
        inputGate.outputGate = this;
    }
    
    public void disconnectInputGate() {
        if (this.inputGate != null) {
            this.inputGate.outputGate = null;
        }
        
        this.inputGate = null;
    }
    
    public Set<CircuitGate> getParentGates() {
        return new HashSet<>(Arrays.asList(inputGate));
    }
    
    public void changeInput(CircuitGate input, CircuitGate newInputGate) {
        this.inputGate = newInputGate;
    }
    
    protected boolean hasInputGate() {
        return inputGate != null;
    }
    
    protected void checkInputGatePresent() {
        if (!hasInputGate()) {
            throw new NoInputGatesException(
                    "This gate does not have two input gates.");
        }
    }
}
