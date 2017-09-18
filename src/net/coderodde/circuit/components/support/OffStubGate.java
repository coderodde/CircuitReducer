package net.coderodde.circuit.components.support;

import net.coderodde.circuit.components.SingularCircuitGate;

public final class OffStubGate extends SingularCircuitGate {

    @Override
    public boolean step() {
        return false;
    }
}
