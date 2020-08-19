package uk.gov.gchq.koryphe.tuple.predicate;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.predicate.PredicateTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class IntegerTupleAdaptedPredicateTest extends PredicateTest<IntegerTupleAdaptedPredicate> {
    // todo this

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