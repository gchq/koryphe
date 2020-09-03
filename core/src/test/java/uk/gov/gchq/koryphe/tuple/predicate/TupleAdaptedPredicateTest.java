package uk.gov.gchq.koryphe.tuple.predicate;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.impl.predicate.IsA;
import uk.gov.gchq.koryphe.impl.predicate.IsMoreThan;
import uk.gov.gchq.koryphe.predicate.PredicateTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TupleAdaptedPredicateTest extends PredicateTest<TupleAdaptedPredicate> {

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        TupleAdaptedPredicate predicate = getInstance();
        String json = "" +
                "{" +
                "   \"selection\": [ \"input\" ]," +
                "   \"predicate\": {" +
                "       \"class\": \"uk.gov.gchq.koryphe.impl.predicate.IsA\"," +
                "       \"type\": \"java.lang.String\"" +
                "   }" +
                "}";

        // When
        String serialised = JsonSerialiser.serialise(predicate);
        TupleAdaptedPredicate deserialised = JsonSerialiser.deserialise(json, TupleAdaptedPredicate.class);

        // Then
        JsonSerialiser.assertEquals(json, serialised);
        assertEquals(predicate, deserialised);
    }

    @Override
    protected TupleAdaptedPredicate getInstance() {
        return new TupleAdaptedPredicate(new IsA(String.class), new String[] { "input" });
    }

    @Override
    protected Iterable<TupleAdaptedPredicate> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new TupleAdaptedPredicate(new IsA(Long.class), new String[] { "input" }),
                new TupleAdaptedPredicate(new IsMoreThan(5), new String[] { "input" }),
                new TupleAdaptedPredicate(new IsA(String.class), new String[] { "Different" })
        );
    }
}