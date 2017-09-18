package net.coderodde.circuit.components.support;

import java.util.Collections;
import java.util.Set;
import net.coderodde.circuit.components.CircuitGate;

public final class InputGate extends CircuitGate {

    private boolean bit;
    
    public Set<CircuitGate> getParentGates() {
        return Collections.<CircuitGate>emptySet();
    }
    
    public InputGate() {
        
    }
    
    public InputGate(boolean bit) {
        setBit(bit);
    }
    
    public void setBit(boolean bit) {
        this.bit = bit;
    }

    @Override
    public boolean step() {
        return bit;
    }
    
    boolean getBit() {
        return bit;
    }

    @Override
    public void changeInput(CircuitGate input, CircuitGate newInput) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}
