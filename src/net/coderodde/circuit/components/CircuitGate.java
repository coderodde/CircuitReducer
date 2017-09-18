package net.coderodde.circuit.components;

import java.util.Set;

public abstract class CircuitGate {

    protected CircuitGate outputGate;
    
    public abstract boolean step();
    
    public abstract Set<CircuitGate> getParentGates();
    
    public abstract void changeInput(CircuitGate input, CircuitGate newInput);
}
