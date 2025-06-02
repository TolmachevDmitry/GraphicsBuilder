package com.tolmic.puzzle15;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.tolmic.graphicsbuilder.predicate.CurrentArguments;
import com.tolmic.graphicsbuilder.predicate.ElementaryPredicate;
import com.tolmic.graphicsbuilder.predicate.Predicate;

public class MainTest {

    @Test
    private void ElementaryPredicateTest() {
        double expectedValue = 12.3;

        CurrentArguments ca = new CurrentArguments();
        Predicate ep = new ElementaryPredicate(0, () -> ca.getX());

        ca.setX(expectedValue);
        ep.calculate();
        
        assertEquals(expectedValue, ep.getValue());
    }

}
