package com.tolmic.graphicsbuilder;

import java.util.Objects;

import com.tolmic.graphicsbuilder.Exception.IncorrectFormulaException;
import com.tolmic.graphicsbuilder.predicate.BinaryPredicate;
import com.tolmic.graphicsbuilder.predicate.CurrentArguments;
import com.tolmic.graphicsbuilder.predicate.ElementaryPredicate;
import com.tolmic.graphicsbuilder.predicate.Predicate;
import com.tolmic.graphicsbuilder.predicate.UnaryPredicate;


/**
 * This class intended for interpret string math formula and calculate values for everyone x by it. 
 * 
 * @author Tolmachev Dmitry
 */
public class SolveTree {

    private String[] symbolPatterns = new String[] {"sciontan", "0123456789."};

    private Predicate solveTree = null;

    private CurrentArguments currentArguments;

    private Predicate findOneLetterOperator(String symb, int currLevel) {
        if (symb.equals("+") || symb.equals("-")) {
            if (symb.equals("+")) {
                return new BinaryPredicate(currLevel, (x1, x2) -> x1 + x2);
            } else if (symb.equals("-")) {
                return new BinaryPredicate(currLevel, (x1, x2) -> x1 - x2);
            }
        } else if (symb.equals("*") || symb.equals("/")) {
            if (symb.equals("*")) {
                return new BinaryPredicate(currLevel + 1, (x1, x2) -> x1 * x2);
            } else if (symb.equals("/")) {
                return new BinaryPredicate(currLevel + 1, (x1, x2) -> x1 / x2);
            }
        } else if (symb.equals("^")) {
            return new BinaryPredicate(currLevel + 3, (x, p) -> Math.pow(x, p));
        } else if (symb.equals("|")) {
            return new UnaryPredicate(currLevel + 3, x -> Math.abs(x));
        } else if (symb.equals("e")) {
            return new UnaryPredicate(currLevel + 10, x -> Math.E);
        } else if (symb.equals("x")) {
            return new ElementaryPredicate(currLevel, () -> currentArguments.getX());
        } else if ("abcdefghijklmnopqrstuvwxyz".indexOf(symb) != -1) {
            return new ElementaryPredicate(currLevel, () -> currentArguments.getParameterValue(symb));
        }

        return null;
    }

    private Predicate findManyLetterOperator(String symb, int currLevel) {
        if (symb.equals("sin")) {
            return new UnaryPredicate(currLevel + 3, x -> Math.sin(x));
        } else if (symb.equals("cos")) {
            return new UnaryPredicate(currLevel + 3, x -> Math.cos(x));
        } else if (symb.equals("tan")) {
            return new UnaryPredicate(currLevel + 3, x -> Math.sin(x) / Math.cos(x));
        } else if (symbolPatterns[1].indexOf(symb) != -1) {
            return new ElementaryPredicate(currLevel + 10, () -> Double.parseDouble(symb));
        }

        return null;
    }

    private Predicate defindOperator(String symb, int currLevel) {
        int n = symb.length();

        return n == 1 ? findOneLetterOperator(symb, currLevel) : findManyLetterOperator(symb, currLevel);
    }

    private void putPredicateToHierarchy(Predicate func) {
        if (Objects.isNull(solveTree)) {
            solveTree = func;

            return;
        }

        if (solveTree.getPriority() >= func.getPriority()) {
            func.setLeft(func);
            solveTree = func;

            return;
        }

        Predicate curr = solveTree;
        while (Objects.nonNull(curr.getRight()) && curr.getRight().getPriority() < func.getPriority()) {
            curr = curr.getRight();
        }

        func.setLeft(curr.getRight());
        curr.setRight(func);
    }

    private String defindCurrPattern(String s) {
        for (String pattern : symbolPatterns) {
            if (pattern.indexOf(s) == 0) {
                return pattern;
            }
        }

        return "";
    }

    private void checkBinarBeginning(String word, String s) throws IncorrectFormulaException {
        if (Objects.nonNull(defindCurrPattern(s))) {
            throw new IncorrectFormulaException("Too erly operator \'" + s + "\', expected full name of predicate \'" + 
                                                 word + "\' and argument after he");
        }
    }

    private void checkBeginningOther(String word, String s, String currPattern) throws IncorrectFormulaException {
        if (currPattern.indexOf(s) < 0) {
            throw new IncorrectFormulaException("Unknown function \'" + word + "\'");
        }
    }

    private void chekErlyUnar(String word) throws IncorrectFormulaException {
        if (word.length() == 1 && Objects.isNull(solveTree)) {
            throw new IncorrectFormulaException("Missing left argument for operator \'" + word + "\'.");
        }
    }

    private void checkPositiveLevel(int level, int currInd) throws IncorrectFormulaException {
        if (level < 0) {
            throw new IncorrectFormulaException("Extra bracket in col " + currInd + ".");
        }
    }

    private void checkClosedBraket(int finalLevel) throws IncorrectFormulaException {
        if (finalLevel > 0) {
            throw new IncorrectFormulaException("Not closed braket !");
        }
    }

    private String[] getStringArray(String str) {
        return str.chars().mapToObj(c -> String.valueOf((char) c)).toArray(String[]::new);
    }

    /**
     * If in system term like given is defined, then it put to solve-tree.
     * 
     * @param func - name of condidate to defined term.
     * @param level - current nesting level. 
     * @return If term is known in system and puted to solev-tree - True, else - False.
     * @throws IncorrectFormulaException if term is unar and for he do not writen left argument.
     */
    private boolean considerToAdding(String func, int level) throws IncorrectFormulaException {
        Predicate predicate = defindOperator(func, level);

        if (Objects.nonNull(predicate)) {
            chekErlyUnar(func);
            putPredicateToHierarchy(predicate);

            return true;
        }

        return false;
    }

    /**
     * Building solve-tree for calculate value of function for concrete point x.
     * 
     * @param formula - formula, writen by user in text-field. For exemple - x^2 + cos(2*x)^x.
     * @throws IncorrectFormulaException arises in case if user uncorrect writen formula.
     */
    public void build(String formula) throws IncorrectFormulaException {
        solveTree = null;

        String[] symbols = getStringArray(formula);
        int size = symbols.length;

        int level = 0;
        String currPattern = "";
        String word = "";
        
        for (int i = 0; i < size; i++) {
            String s = symbols[i];

            if (word.length() > 0) {
                checkBinarBeginning(word, s);
                checkBeginningOther(word, s, currPattern);
            } else {
                currPattern = defindCurrPattern(s);
            }

            if ("() ".equals(s)) {
                if (!s.equals(" ")) {
                    level += s.equals("(") ? 1 : -1;
                    checkPositiveLevel(level, i);
                }

                continue;
            }

            word += s;
            if (considerToAdding(word, level)) {
                word = "";
            }
        }
        
        checkClosedBraket(level);
    }

    /**
     * Calculate values for concrete x.
     * 
     * @return massive of calculated values.
     */
    public double calculate() {
        // нужен обход в ширину

        return solveTree.getValue();
    }

}
