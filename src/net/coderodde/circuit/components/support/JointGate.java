package net.coderodde.circuit.components.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.coderodde.circuit.components.CircuitGate;
import net.coderodde.circuit.components.NoInputGatesException;
import net.coderodde.circuit.components.SingularCircuitGate;

public class JointGate extends SingularCircuitGate {

    private CircuitGate inputGate;
    private final List<CircuitGate> outputGates = new ArrayList<>(2);
    
    public void setInputGate(CircuitGate inputGate) {
        this.inputGate = Objects.requireNonNull(inputGate,
                                                "The input gate is null.");
    }
    
    public void addOutputGate(CircuitGate outputGate) {
        this.outputGates.add(
                Objects.requireNonNull(outputGate, "The output gate is null."));
    }
    
    public void removeOutputGate(CircuitGate outputGate) {
        this.outputGates.remove(
                Objects.requireNonNull(outputGate, "The output gate is null."));
    }
    
    @Override
    public boolean step() {
        checkInputGatePresent();
        return inputGate.step();
    }
}
