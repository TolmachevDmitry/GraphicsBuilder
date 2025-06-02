package com.tolmic.graphicsbuilder.predicate;

import java.util.function.Supplier;

/**
 * Wrapper over elementary elements of notation: number, constant, parameter.
 * 
 * @author Dmitry Tolmachev
 */
public class ElementaryPredicate extends Predicate {

    private Supplier<Double> supplier;

    public ElementaryPredicate(int priority, Supplier<Double> operator) {
        super(priority, PredicateType.ELEMENTARY_ELEMENT);
        
        this.supplier = operator;
    }

    @Override
    protected double getResult() {
        return supplier.get();
    }

}
