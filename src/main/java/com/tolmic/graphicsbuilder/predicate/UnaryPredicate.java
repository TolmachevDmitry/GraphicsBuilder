package com.tolmic.graphicsbuilder.predicate;

import java.util.function.UnaryOperator;


/**
 * Wrapper over unary function
 * 
 * @author Dmitry Tolmachev
 */
public class UnaryPredicate extends Predicate {

    private UnaryOperator<Double> operator;

    public UnaryPredicate(int priority, UnaryOperator<Double> operator) {
        super(priority, PredicateType.UNARY_OPERATOR);
    }

    @Override
    protected double getResult() {
        Double argue = null;

        if (getLeft() != null) {
            argue = getLeft().getValue();
        } 

        if (getRight() != null) {
            argue = getRight().getValue();
        }

        return operator.apply(argue);
    }

    public UnaryOperator<Double> getOperator() {
        return operator;
    }

}
