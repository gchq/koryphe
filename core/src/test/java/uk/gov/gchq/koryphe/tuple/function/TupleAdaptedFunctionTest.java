package uk.gov.gchq.koryphe.tuple.function;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.impl.function.ParseDate;
import uk.gov.gchq.koryphe.impl.function.ToLong;
import uk.gov.gchq.koryphe.impl.function.ToUpperCase;
import uk.gov.gchq.koryphe.tuple.ArrayTuple;
import uk.gov.gchq.koryphe.tuple.MapTuple;
import uk.gov.gchq.koryphe.tuple.Tuple;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

class TupleAdaptedFunctionTest extends FunctionTest<TupleAdaptedFunction> {

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] { Tuple.class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] { Tuple.class };
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        TupleAdaptedFunction instance = getInstance();
        String json = "" +
                "{" +
                    "\"selection\": [ \"input\" ]," +
                    "\"function\": {" +
                        "\"class\": \"uk.gov.gchq.koryphe.impl.function.ToUpperCase\"" +
                    "}," +
                    "\"projection\": [ \"output\" ]" +
                "}";

        // When
        String serialised = JsonSerialiser.serialise(instance);
        TupleAdaptedFunction deserialised = JsonSerialiser.deserialise(json, TupleAdaptedFunction.class);

        // Then
        JsonSerialiser.assertEquals(json, serialised);
        assertEquals(instance, deserialised);
    }

    @Override
    protected TupleAdaptedFunction getInstance() {
        return new TupleAdaptedFunction(new String[] {"input"}, new ToUpperCase(), new String[] { "output" });
    }

    @Override
    protected Iterable<TupleAdaptedFunction> getDifferentInstances() {
        return Arrays.asList(
                new TupleAdaptedFunction(new String[] {"differentInput"}, new ToUpperCase(), new String[] { "output" }),
                new TupleAdaptedFunction(new String[] {"input"}, new ToUpperCase(), new String[] { "DifferentOutput" }),
                new TupleAdaptedFunction(new String[] {"input"}, new ParseDate(), new String[] { "output" }),
                new TupleAdaptedFunction()
        );
    }

    @Test
    public void shouldErrorIfInputIsTheWrongType() {
        // Given
        TupleAdaptedFunction<String, Object, Long> function = new TupleAdaptedFunction<>(new String[]{"input"}, new ToLong(), new String[]{"output"});
        MapTuple<String> inputs = new MapTuple<>();
        inputs.put("input", "aString");

        // When / Then
        try {
            function.apply(inputs);
            fail("Expected function to fail");
        } catch (NumberFormatException e) {
            assertNotNull(e.getMessage());
        }

    }

    @Test
    public void shouldNotRemoveTheInputFromTheTuple() {
        // Given
        TupleAdaptedFunction<Integer, Object, String> instance = new TupleAdaptedFunction<>(new Integer[]{0}, new ToUpperCase(), new Integer[]{1});
        ArrayTuple objects = new ArrayTuple("test", null);

        // When
        ArrayTuple returnedTuple = (ArrayTuple) instance.apply(objects);

        // Then
        assertEquals("test", returnedTuple.get(0));
        assertEquals("TEST", returnedTuple.get(1));
    }
}