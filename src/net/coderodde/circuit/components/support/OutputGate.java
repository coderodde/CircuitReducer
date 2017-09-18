package net.coderodde.circuit.components.support;

import net.coderodde.circuit.components.CircuitGate;
import net.coderodde.circuit.components.SingularCircuitGate;

public final class OutputGate extends SingularCircuitGate {
    
    private boolean bit;
    
    public boolean getBit() {
        return bit;
    }
    
    public void setInputGate(CircuitGate gate) {
        connectInputGate(gate);
    }
    
    @Override
    public boolean step() {
        checkInputGatePresent();
        bit = inputGate.step();
        return bit;
    }
}
