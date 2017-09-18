package net.coderodde.circuit.components;

import net.coderodde.circuit.components.support.AndGate;
import net.coderodde.circuit.components.support.InputGate;
import net.coderodde.circuit.components.support.NotGate;
import net.coderodde.circuit.components.support.OutputGate;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class CircuitTest {
    
    private Circuit circuit;
    
    public CircuitTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        circuit = new Circuit();
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testMinimizeDoubleNot() {
        InputGate input   = new InputGate();
        NotGate not1      = new NotGate();
        NotGate not2      = new NotGate();
        OutputGate output = new OutputGate();
        
        output.connectInputGate(not2);
        not2.connectInputGate(not1);
        not1.connectInputGate(input);
        
        circuit.addInputGate(input);
        circuit.addOutputGate(output);
        
        Circuit test = new Circuit(circuit);
        
        assertEquals(4, circuit.size());
        circuit.minimize();
        assertEquals(2, circuit.size());
        assertEquals(test, circuit);
    }
    
    @Test
    public void testMinimizedNotNotAnd() {
        InputGate input1 = new InputGate();
        InputGate input2 = new InputGate();
        NotGate not1 = new NotGate();
        NotGate not2 = new NotGate();
        AndGate and = new AndGate();
        OutputGate output = new OutputGate();
        
        output.connectInputGate(and);
        and.connectInputGate(not1);
        and.connectInputGate(not2);
        not1.connectInputGate(input1);
        not2.connectInputGate(input2);
        
        circuit.addInputGate(input1);
        circuit.addInputGate(input2);
        circuit.addOutputGate(output);
        
        Circuit test = new Circuit(circuit);
        
        assertEquals(6, circuit.size());
        circuit.minimize();
        assertEquals(5, circuit.size());
        assertEquals(test, circuit);
    }
}
