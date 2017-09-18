package net.coderodde.circuit.components.support;

import net.coderodde.circuit.components.SingularCircuitGate;

public final class NotGate extends SingularCircuitGate {

    @Override
    public boolean step() {
        checkInputGatePresent();
        return !inputGate.step();
    }
}
