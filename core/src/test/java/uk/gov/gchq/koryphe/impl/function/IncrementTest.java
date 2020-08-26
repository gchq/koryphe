package uk.gov.gchq.koryphe.impl.function;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IncrementTest extends FunctionTest<Increment> {
    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] { Number.class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] { Number.class };
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        String json = "{ \"class\": \"uk.gov.gchq.koryphe.impl.function.Increment\" }";
        Increment increment = new Increment();

        // When
        String serialised = JsonSerialiser.serialise(increment);
        Increment deserialised = JsonSerialiser.deserialise(json, Increment.class);

        // Then
        JsonSerialiser.assertEquals(json, serialised);
        assertEquals(increment, deserialised);
    }

    @Test
    public void shouldReturnIncrementIfInputIsNull() {
        // Given
        Increment increment = new Increment(5);

        // When
        Number value = increment.apply(null);

        // Then
        assertEquals(5, value);
    }

    @Test
    public void shouldReturnInputIfIncrementIsNull() {
        // Given
        Increment increment = new Increment();

        // When
        Number output = increment.apply(8);

        // Then
        assertEquals(8, output);
    }

    @Test
    public void shouldMatchOutputTypeWithIncrementType() {
        // Given
        Increment increment = new Increment(10L);

        // When
        Number output = increment.apply(2);

        // Then
        assertEquals(Long.class, output.getClass());
    }

    @Test
    public void shouldIncrementByTheSetIncrement() {
        // Given
        Increment increment = new Increment(10L);

        // When
        Number output = increment.apply(2);

        // Then
        assertEquals(12, output.intValue());
    }

    @Test
    public void shouldBeAbleToHandleDifferentInputAndIncrementTypes() {
        // Given
        Increment increment = new Increment(10.5);

        // When
        Number output = increment.apply(2);

        // Then
        assertEquals(12.5, output);
    }

    @Override
    protected Increment getInstance() {
        return new Increment(3);
    }

    @Override
    protected Iterable<Increment> getDifferentInstances() {
        return Arrays.asList(
                new Increment(3L),
                new Increment(5),
                new Increment());
    }
}
