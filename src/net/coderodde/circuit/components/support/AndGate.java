package net.coderodde.circuit.components.support;

import net.coderodde.circuit.components.DualCircuitGate;

public final class AndGate extends DualCircuitGate {

    @Override
    public boolean step() {
        checkInputGatesPresent();
        return inputGate1.step() && inputGate2.step();
    }
}
