package uk.gov.gchq.koryphe.tuple.predicate;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.predicate.PredicateTest;

import java.io.IOException;

class IntegerTupleAdaptedPredicateTest extends PredicateTest<IntegerTupleAdaptedPredicate> {
    // todo this

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {

    }

    @Override
    protected IntegerTupleAdaptedPredicate getInstance() {
        return null;
    }

    @Override
    protected Iterable<IntegerTupleAdaptedPredicate> getDifferentInstances() {
        return null;
    }
}