package uk.gov.gchq.koryphe.tuple.predicate;

import uk.gov.gchq.koryphe.impl.predicate.IsA;
import uk.gov.gchq.koryphe.impl.predicate.IsMoreThan;
import uk.gov.gchq.koryphe.predicate.PredicateTest;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TupleAdaptedPredicateTest extends PredicateTest<TupleAdaptedPredicate> {

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // todo serialisation check
    }

    @Override
    protected TupleAdaptedPredicate getInstance() {
        return new TupleAdaptedPredicate(new IsA(String.class), new String[] { "input" });
    }

    @Override
    protected Iterable<TupleAdaptedPredicate> getDifferentInstances() {
        return Arrays.asList(
                new TupleAdaptedPredicate(new IsA(Long.class), new String[] { "input" }),
                new TupleAdaptedPredicate(new IsMoreThan(5), new String[] { "input" }),
                new TupleAdaptedPredicate(new IsA(String.class), new String[] { "Different" })
        );
    }

    // todo actual tests
}