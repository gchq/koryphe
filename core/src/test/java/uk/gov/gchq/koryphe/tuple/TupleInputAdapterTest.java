package uk.gov.gchq.koryphe.tuple;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TupleInputAdapterTest extends FunctionTest<TupleInputAdapter> {
    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] { Tuple.class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] { Object.class };
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        TupleInputAdapter<String, String> instance = getInstance();
        String json = "" +
                "{" +
                "   \"class\": \"uk.gov.gchq.koryphe.tuple.TupleInputAdapter\"," +
                "   \"selection\": [ \"input\" ]"+
                "}";

        // When
        String serialised = JsonSerialiser.serialise(instance);
        TupleInputAdapter deserialised = JsonSerialiser.deserialise(json, TupleInputAdapter.class);

        // Then
        JsonSerialiser.assertEquals(json, serialised);
        assertEquals(instance, deserialised);
    }

    @Override
    protected TupleInputAdapter<String, String> getInstance() {
        return new TupleInputAdapter<>(new String[] { "input" });
    }

    @Override
    protected Iterable<TupleInputAdapter> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new TupleInputAdapter(),
                new TupleInputAdapter(new String[] { "differentInput" }),
                new TupleInputAdapter(new Integer[] { 1, 2, 3 })
        );
    }

    @Test
    public void shouldReturnObjectIfSingleSelectionIsProvided() {
        // Given
        MapTuple<String> objects = new MapTuple<>();
        objects.put("one", 1);
        objects.put("two", 2);

        TupleInputAdapter<String, Object> inputAdapter = new TupleInputAdapter<>(new String[]{"one"});

        // When
        Object adapted = inputAdapter.apply(objects);

        // Then
        assertEquals(1, adapted);
    }

    @Test
    public void shouldReturnNullIfTheObjectReferencedDoesntExist() {
        // Given
        MapTuple<String> objects = new MapTuple<>();
        objects.put("one", 1);
        objects.put("two", 2);

        TupleInputAdapter<String, Object> inputAdapter = new TupleInputAdapter<>(new String[]{ "three" });

        // When
        Object adapted = inputAdapter.apply(objects);

        // Then
        assertNull(adapted);
    }

    @Test
    public void shouldReturnAReferenceArrayTupleIfMoreThanOneSelectionIsProvided() {
        // Given
        MapTuple<String> objects = new MapTuple<>();
        objects.put("one", 1);
        objects.put("two", 2);
        objects.put("three", 3);

        TupleInputAdapter<String, Object> inputAdapter = new TupleInputAdapter<>(new String[]{ "one", "two" });

        // When
        Object adapted = inputAdapter.apply(objects);

        // Then
        ReferenceArrayTuple<String> expected = new ReferenceArrayTuple<>(objects, new String[]{"one", "two"});
        assertEquals(expected, adapted);
    }
}