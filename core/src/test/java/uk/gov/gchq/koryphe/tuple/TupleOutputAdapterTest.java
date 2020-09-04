package uk.gov.gchq.koryphe.tuple;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.util.EqualityTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TupleOutputAdapterTest extends EqualityTest<TupleOutputAdapter> { // Can't extend FunctionTest as TupleOutputAdapter is a BiFunction rather than a Function.

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        TupleOutputAdapter instance = getInstance();
        String json = "" +
                "{" +
                "   \"class\": \"uk.gov.gchq.koryphe.tuple.TupleOutputAdapter\"," +
                "   \"projection\": [ \"input\" ]" +
                "}";

        // When
        String serialised = JsonSerialiser.serialise(instance);
        TupleOutputAdapter deserialised = JsonSerialiser.deserialise(json, TupleOutputAdapter.class);

        // Then
        JsonSerialiser.assertEquals(json, serialised);
        assertEquals(instance, deserialised);
    }

    @Override
    protected TupleOutputAdapter getInstance() {
        return new TupleOutputAdapter(new String[] { "input" });
    }

    @Override
    protected Iterable<TupleOutputAdapter> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new TupleOutputAdapter(new String[] { "Different" }),
                new TupleOutputAdapter(new Integer[] { 1, 2, 3 }),
                new TupleOutputAdapter()
        );
    }

    @Test
    public void shouldAddToTuple() {
        // Given
        ArrayTuple state = new ArrayTuple(3);
        state.put(0, "thing");
        state.put(1, 50L);

        // When
        TupleOutputAdapter<Integer, Object> adapter = new TupleOutputAdapter<>(new Integer[]{2});
        Tuple<Integer> adapted = adapter.apply(state, "test");

        // Then
        assertEquals("test", adapted.get(2));
    }

    @Test
    public void shouldReplaceAnyExistingValueInTuple() {
        // Given
        ArrayTuple state = new ArrayTuple(3);
        state.put(0, "thing");
        state.put(1, 50L);
        state.put(2, "Replace me");

        // When
        TupleOutputAdapter<Integer, Object> adapter = new TupleOutputAdapter<>(new Integer[]{2});
        Tuple<Integer> adapted = adapter.apply(state, "test");

        // Then
        assertEquals("test", adapted.get(2));
    }
}