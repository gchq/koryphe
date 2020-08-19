package uk.gov.gchq.koryphe.impl.binaryoperator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.binaryoperator.BinaryOperatorTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StringConcatTest extends BinaryOperatorTest<StringConcat> {

    private String state;

    @BeforeEach
    public void before() {
        state = null;
    }

    @Test
    public void shouldConcatStringsTogether() {
        // Given
        final StringConcat function = new StringConcat();
        function.setSeparator(";");

        // When
        state = function.apply(state, "1");
        state = function.apply(state, "2");
        state = function.apply(state, null);

        // Then
        assertEquals("1;2", state);
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final StringConcat function = new StringConcat();

        // When 1
        final String json = JsonSerialiser.serialise(function);

        // Then 1
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.binaryoperator.StringConcat\",%n" +
                "  \"separator\" : \",\"%n" +
                "}"), json);

        // When 2
        final StringConcat deserialisedAggregator = JsonSerialiser.deserialise(json, getFunctionClass());

        // Then 2
        assertNotNull(deserialisedAggregator);
    }

    @Override
    protected StringConcat getInstance() {
        return new StringConcat();
    }

    @Override
    protected Iterable<StringConcat> getDifferentInstances() {
        StringConcat alternative = new StringConcat();
        alternative.setSeparator(" ");
        return Collections.singletonList(alternative);
    }

    @Override
    protected Class<StringConcat> getFunctionClass() {
        return StringConcat.class;
    }
}
