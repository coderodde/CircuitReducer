package net.coderodde.circuit;

import net.coderodde.circuit.components.Circuit;
import net.coderodde.circuit.components.support.AndGate;
import net.coderodde.circuit.components.support.InputGate;
import net.coderodde.circuit.components.support.NotGate;
import net.coderodde.circuit.components.support.OrGate;
import net.coderodde.circuit.components.support.OutputGate;

public final class Demo {

    private Demo() {}
    
    public static void main(String[] args) {
        InputGate input = new InputGate();
        NotGate not1 = new NotGate();
        NotGate not2 = new NotGate();
        OutputGate output = new OutputGate();
        output.connectInputGate(not2);
        not2.connectInputGate(not1);
        not1.connectInputGate(input);
        
        Circuit c = new Circuit();
        c.addInputGate(input);
        c.addOutputGate(output);
        
        Circuit copy = new Circuit(c);
        
        System.out.println(c.size());
        c.minimize();
        System.out.println(c.size());
        
        System.out.println("Equals: " + c.equals(copy));
        
//        System.exit(0);
//        
//        AndGate andGate = new AndGate();
//        OrGate orGate   = new OrGate();
//        NotGate notGate = new NotGate();
//        InputGate inputGate1 = new InputGate();
//        InputGate inputGate2 = new InputGate();
//        InputGate inputGate3 = new InputGate();
//        OutputGate outputGate = new OutputGate();
//        
//        orGate.connectInputGate(inputGate1);
//        orGate.connectInputGate(inputGate2);
//        notGate.connectInputGate(inputGate3);
//        andGate.connectInputGate(orGate);
//        andGate.connectInputGate(notGate);
//        outputGate.connectInputGate(andGate);
//        
//        for (boolean b1 : new boolean[]{ false, true }) {
//            inputGate1.setBit(b1);
//            
//            for (boolean b2 : new boolean[]{ false, true }) {
//                inputGate2.setBit(b2);
//                
//                for (boolean b3 : new boolean[]{ false, true }) {
//                    inputGate3.setBit(b3);
//                    System.out.println(b1 + ", " +
//                                       b2 + ", " + 
//                                       b3 + " = " + outputGate.step());
//                }
//            }
//        }
    }
}
