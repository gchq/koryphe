package uk.gov.gchq.koryphe.impl.function;

import com.google.common.collect.Lists;
import org.junit.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class IterableLongestTest extends FunctionTest {

    @Test
    public void shouldHandleNullInput() {
        // Given
        final IterableLongest function = getInstance();
        final Iterable input = null;

        // When
        final Object result = function.apply(input);

        // Then
        assertNull(result);
    }

    @Test
    public void shouldGetLongestItemFromList() {
        // Given
        final IterableLongest function = getInstance();
        final List<String> list = Lists.newArrayList("a", "ab", "abc");

        // When
        final Object result = function.apply(list);

        // Then
        assertEquals("abc", result);
    }


    @Override
    protected IterableLongest getInstance() {
        return new IterableLongest();
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return IterableLongest.class;
    }

    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final IterableLongest function = new IterableLongest();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "   \"class\" : \"uk.gov.gchq.koryphe.impl.function.IterableLongest\"%n" +
                "}"), json);

        // When 2
        final IterableLongest deserialised = JsonSerialiser.deserialise(json, IterableLongest.class);

        // Then
        assertNotNull(deserialised);
    }
}
