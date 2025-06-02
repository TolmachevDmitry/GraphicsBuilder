package com.tolmic.graphicsbuilder;

import java.util.Arrays;
import java.util.List;

public class FormulasInterpretator {

    SolveTree solveTree = new SolveTree();

    public List<String> operators = Arrays.asList("*", "/", "+", "-", "e", "^", 
                                        "pi", "cos", "sin", "tg", "ctg", "log",
                                        "lg", "ln");
    
    public List<String> primaryOperators = Arrays.asList("+", "-", "*", "/");
    
    public void parseFormula(String formula) {
        
    }

    public void calculate(double a, double b, double h) {
        solveTree.calculate();
    }
}
