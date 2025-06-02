package com.tolmic.graphicsbuilder.predicate;

import java.util.function.BinaryOperator;

/**
 * Wrapper over binary function.
 * 
 * @author Dmitry Tolmachev
 */
public class BinaryPredicate extends Predicate {

    BinaryOperator<Double> operator;

    public BinaryPredicate(int priority, BinaryOperator<Double> operator) {
        super(priority, PredicateType.BINARY_OPERATOR);

        this.operator = operator;
    }

    @Override
    public double getResult() {
        return operator.apply(getLeft().getValue(), getRight().getValue());
    }

}
