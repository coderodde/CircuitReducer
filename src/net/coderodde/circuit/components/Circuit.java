package net.coderodde.circuit.components;

import java.util.ArrayDeque;
import net.coderodde.circuit.components.support.OutputGate;
import net.coderodde.circuit.components.support.InputGate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.coderodde.circuit.components.support.AndGate;
import net.coderodde.circuit.components.support.JointGate;
import net.coderodde.circuit.components.support.NotGate;
import net.coderodde.circuit.components.support.OffStubGate;
import net.coderodde.circuit.components.support.OnStubGate;
import net.coderodde.circuit.components.support.OrGate;

public final class Circuit {

    private final List<InputGate> inputGates = new ArrayList<>();
    private final List<OutputGate> outputGates = new ArrayList<>();

    public Circuit() {
        
    }
    
    public Circuit(Circuit other) {
        Objects.requireNonNull(other, "The input circuit is null.");
        
        Deque<CircuitGate> queue = new ArrayDeque<>();
        Set<CircuitGate> closed = new HashSet<>();
        Map<CircuitGate, CircuitGate> map = new HashMap<>();
        
        for (InputGate inputGate : other.inputGates) {
            InputGate copyInputGate = new InputGate();
            map.put(inputGate, copyInputGate);
            this.inputGates.add(copyInputGate);
        }
        
        for (OutputGate outputGate : other.outputGates) {
            OutputGate copyOutputGate = new OutputGate();
            map.put(outputGate, copyOutputGate);
            this.outputGates.add(copyOutputGate);
        }
        
        queue.addAll(other.outputGates);
        closed.addAll(other.outputGates);
        
        while (!queue.isEmpty()) {
            CircuitGate current = queue.removeFirst();
            
            for (CircuitGate parentGate : current.getParentGates()) {
                if (!closed.contains(parentGate)) {
                    closed.add(parentGate);
                    queue.addLast(parentGate);
                    CircuitGate copyParentGate = copy(parentGate);
                    map.put(parentGate, copyParentGate);
                }
            }
        }
        
        // Reconnect all the wires:
        for (CircuitGate gate : closed) {
            if (gate instanceof DualCircuitGate) {
                DualCircuitGate dcg = (DualCircuitGate) gate;
                DualCircuitGate dcgThis = (DualCircuitGate) map.get(gate);
                dcgThis.connectInputGate(map.get(dcg.inputGate1));
                dcgThis.connectInputGate(map.get(dcg.inputGate2));
            } else if (gate instanceof SingularCircuitGate) {
                SingularCircuitGate scg = (SingularCircuitGate) gate;
                SingularCircuitGate scgThis = 
                        (SingularCircuitGate) map.get(gate);
                scgThis.connectInputGate(map.get(scg.inputGate));
            }
        }
    }
    
    private CircuitGate copy(CircuitGate gate) {
        if (gate instanceof AndGate) {
            return new AndGate();
        } else if (gate instanceof JointGate) {
            return new JointGate();
        } else if (gate instanceof NotGate) {
            return new NotGate();
        } else if (gate instanceof OffStubGate) {
            return new OffStubGate();
        } else if (gate instanceof OnStubGate) {
            return new OnStubGate();
        } else if (gate instanceof OrGate) {
            return new OrGate();
        } else if (gate instanceof InputGate) {
            return new InputGate();
        } else {
            throw new IllegalStateException("Unknown gate.");
        }
    }
    
    public void addInputGate(InputGate inputGate) {
        this.inputGates.add(inputGate);
    }

    public void addOutputGate(OutputGate outputGate) {
        this.outputGates.add(outputGate);
    }

    public void setInput(boolean... bits) {
        for (int i = 0; i < Math.min(bits.length, inputGates.size()); ++i) {
            inputGates.get(i).setBit(bits[i]);
        }
    }

    public int size() {
        Deque<CircuitGate> queue = new ArrayDeque<>();
        Set<CircuitGate> closed = new HashSet<>();

        queue.addAll(outputGates);
        closed.addAll(outputGates);

        while (!queue.isEmpty()) {
            CircuitGate current = queue.removeFirst();

            for (CircuitGate parentGate : current.getParentGates()) {
                if (!closed.contains(parentGate)) {
                    closed.add(parentGate);
                    queue.addLast(parentGate);
                }
            }
        }
        
        return closed.size();
    }

    public boolean[] readOutput() {
        boolean[] output = new boolean[outputGates.size()];

        for (int i = 0; i < output.length; ++i) {
            output[i] = outputGates.get(i).getBit();
        }

        return output;
    }

    public InputGate getInputGate(int index) {
        return inputGates.get(index);
    }

    public OutputGate getOutputGate(int index) {
        return outputGates.get(index);
    }
    
    public int getNumberOfInputs() {
        return inputGates.size();
    }
    
    public int getNumberOfOutputs() {
        return outputGates.size();
    }

    public void step() {
        for (OutputGate outputGate : outputGates) {
            outputGate.step();
        }
    }

    public void minimize() {
        new CircuitMinimizer().run();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        
        if (o == this) {
            return true;
        }
        
        if (!getClass().equals(o.getClass())) {
            return false;
        }
        
        Circuit other = (Circuit) o;
        
        if (getNumberOfInputs() != other.getNumberOfInputs() 
                || getNumberOfOutputs() != other.getNumberOfOutputs()) {
            return false;
        }
        
        boolean[] inputBits = new boolean[getNumberOfInputs()];
        
        while (inc(inputBits)) {
            if (!Arrays.equals(readOutput(), other.readOutput())) {
                return false;
            }
        }
        
        return true;
    }
    
    private boolean inc(boolean[] inputBits) {
        for (int i = 0; i < inputBits.length; ++i) {
            if (inputBits[i] == false) {
                inputBits[i] = true;
                return true;
            } else {
                inputBits[i] = false;
            }
        }
        
        return false;
    }
    
    private final class CircuitMinimizer {

        private final Set<CircuitGate> allGates = new HashSet<>();
        private final Map<CircuitGate, CircuitGate> parents = new HashMap<>();

        CircuitMinimizer() {
            allGates.addAll(outputGates);
            expandCircuitGraph();
        }

        void run() {
            while (true) {
                if (tryMinimizeDoubleNot()) {
                    continue;
                }
                
                if (tryMinimizeNotNotAnd()) {
                    continue;
                }
                
                return;
            }
        }
        
        private boolean tryMinimizeNotNotAnd() {
            return false;
        }

        private boolean tryMinimizeDoubleNot() {
            List<CircuitGate> gateList = new ArrayList<>(allGates);
            
            for (CircuitGate gate : gateList) {
                if (!(gate instanceof NotGate)) {
                    continue;
                }

                NotGate notGate = (NotGate) gate;
                CircuitGate parentOfNotGate = notGate.inputGate;

                if (!(parentOfNotGate instanceof NotGate)) {
                    continue;
                }

                NotGate parent = (NotGate) parentOfNotGate;
                CircuitGate child = notGate.outputGate;
                parent.inputGate.outputGate = notGate.outputGate;
                child.changeInput(notGate, parent.inputGate);
                allGates.remove(notGate);
                allGates.remove(parent);
                return true;
            }

            return false;
        }

        private void expandCircuitGraph() {
            Deque<CircuitGate> queue = new ArrayDeque<>();
            Set<CircuitGate> closed = new HashSet<>();

            for (CircuitGate gate : allGates) {
                parents.put(gate, null);
            }

            queue.addAll(allGates);
            closed.addAll(allGates);

            while (!queue.isEmpty()) {
                CircuitGate current = queue.removeFirst();

                for (CircuitGate parentGate : current.getParentGates()) {
                    if (!closed.contains(parentGate)) {
                        closed.add(parentGate);
                        queue.addLast(parentGate);
                        parents.put(parentGate, current);
                    }
                }
            }

            allGates.addAll(closed);
        }
    }
}
