package uk.gov.gchq.koryphe.tuple.predicate;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.impl.predicate.Exists;
import uk.gov.gchq.koryphe.impl.predicate.IsA;
import uk.gov.gchq.koryphe.impl.predicate.Not;
import uk.gov.gchq.koryphe.impl.predicate.StringContains;
import uk.gov.gchq.koryphe.predicate.PredicateTest;
import uk.gov.gchq.koryphe.tuple.MapTuple;
import uk.gov.gchq.koryphe.tuple.n.Tuple1;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntegerTupleAdaptedPredicateTest extends PredicateTest<IntegerTupleAdaptedPredicate> {

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        IntegerTupleAdaptedPredicate predicate = getInstance();
        String json = "" +
                "{" +
                "   \"selection\": [ 1 ]," +
                "   \"predicate\": {" +
                "       \"class\": \"uk.gov.gchq.koryphe.impl.predicate.StringContains\"," +
                "       \"value\": \"test\"," +
                "       \"ignoreCase\": false" +
                "   }" +
                "}";

        // When
        String serialised = JsonSerialiser.serialise(predicate);
        IntegerTupleAdaptedPredicate deserialised = JsonSerialiser.deserialise(json, IntegerTupleAdaptedPredicate.class);

        // Then
        JsonSerialiser.assertEquals(json, serialised);
        assertEquals(predicate, deserialised);
    }

    @Override
    protected IntegerTupleAdaptedPredicate getInstance() {
        return new IntegerTupleAdaptedPredicate(new StringContains("test"), 1);
    }

    @Override
    protected Iterable<IntegerTupleAdaptedPredicate> getDifferentInstances() {
        return Arrays.asList(
                new IntegerTupleAdaptedPredicate(new StringContains("different"), 1),
                new IntegerTupleAdaptedPredicate(new StringContains("test"), 2),
                new IntegerTupleAdaptedPredicate(new StringContains("test"), 1, 2),
                new IntegerTupleAdaptedPredicate()
        );
    }

    @Test
    public void shouldWorkWithMapTuplesWhenKeysAreIntegers() {
        // Given
        MapTuple<Integer> input = new MapTuple<>();
        input.put(0, "test");

        // When
        IntegerTupleAdaptedPredicate predicate = new IntegerTupleAdaptedPredicate(new IsA(String.class), 0);

        // Then
        assertTrue(predicate.test(input));
    }

    @Test
    public void shouldPassNullIfReferenceDoesNotExist() {
        // Given
        MapTuple<Integer> input = new MapTuple<>();
        input.put(0, "test");

        // When
        IntegerTupleAdaptedPredicate predicate = new IntegerTupleAdaptedPredicate(new Not<>(new Exists()), 1);

        // Then
        assertTrue(predicate.test(input));
    }

    @Test
    public void shouldEvaluateTupleReferenceAndApplyPredicate() {
        // Given
        Tuple1<Object> input = new Tuple1<>();
        input.put(0, "test");

        // When
        IntegerTupleAdaptedPredicate predicate = new IntegerTupleAdaptedPredicate(new StringContains("te"), 0);

        // Then
        assertTrue(predicate.test(input));
    }
}