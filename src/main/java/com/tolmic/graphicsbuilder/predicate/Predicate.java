package com.tolmic.graphicsbuilder.predicate;

/**
 * This basic-class to store information about elementary operation and his predecessors and calculation.
 * 
 * @author Dmitry Tolmachev
 */
public abstract class Predicate {

    private double value;

    private int priority;

    private Predicate left;
    private Predicate right;

    private PredicateType predicateType;

    protected abstract double getResult();

    /**
     * 
     * @return result of calculation of this predicate.
     */
    public void calculate() {
        this.value = getResult();
    }

    public Predicate(int priority, PredicateType predicateType) {
        this.priority = priority;
        this.predicateType = predicateType;
    }

    public double getValue() {
        return value;
    }

    public Predicate getLeft() {
        return left;
    }

    public Predicate getRight() {
        return right;
    }

    public void setLeft(Predicate child) {
        this.left = child;
    }

    public void setRight(Predicate child) {
        this.right = child;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public PredicateType getPredicateType() {
        return predicateType;
    }
}
