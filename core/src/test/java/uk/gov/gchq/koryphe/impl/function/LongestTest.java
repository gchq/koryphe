package uk.gov.gchq.koryphe.impl.function;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.junit.Test;
import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class LongestTest extends FunctionTest {

    @Test
    public void shouldHandleNullInputs() {
        // Given
        final Longest function = getInstance();
        final Object input1 = null;
        final Object input2 = null;

        // When
        final Object result = function.apply(input1, input2);

        // Then
        assertNull(result);
    }

    @Test
    public void shouldThrowExceptionForIncompatibleInputType() {
        // Given
        final Longest function = getInstance();
        final Object input1 = new Concat();
        final Object input2 = new Concat();

        // When / Then
        try {
            function.apply(input1, input2);
            fail("Exception expected");
        } catch (final IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Could not determine the size of the provided value"));
        }
    }

    @Test
    public void shouldReturnLongestStringInput() {
        // Given
        final Longest<String> function = new Longest<>();
        final String input1 = "A short string";
        final String input2 = "A longer string";

        // When
        final String result = function.apply(input1, input2);

        // Then
        assertEquals(input2, result);
    }

    @Test
    public void shouldReturnLongestObjectArrayInput() {
        // Given
        final Longest<Object[]> function = new Longest<>();
        final Object[] input1 = new Object[5];
        final Object[] input2 = new Object[10];

        // When
        final Object[] result = function.apply(input1, input2);

        // Then
        assertArrayEquals(input2, result);
    }

    @Test
    public void shouldReturnLongestListInput() {
        // Given
        final Longest<List<Integer>> function = new Longest<>();
        final List<Integer> input1 = Lists.newArrayList(1);
        final List<Integer> input2 = Lists.newArrayList(1, 2, 3);

        // When
        final List<Integer> result = function.apply(input1, input2);

        // Then
        assertEquals(input2, result);
    }

    @Test
    public void shouldReturnLongestSetInput() {
        // Given
        final Longest<Set<Integer>> function = new Longest<>();
        final Set<Integer> input1 = Sets.newHashSet(1);
        final Set<Integer> input2 = Sets.newHashSet(1, 2, 3);

        // When
        final Set<Integer> result = function.apply(input1, input2);

        // Then
        assertEquals(input2, result);
    }

    @Test
    public void shouldReturnLongestMapInput() {
        // Given
        final Longest<Map<String,String>> function = new Longest<>();
        final Map<String,String> input1 = new HashMap<>();
        final Map<String,String> input2 = Maps.asMap(Sets.newHashSet("1"), k -> k);

        // When
        final Map<String,String> result = function.apply(input1, input2);

        // Then
        assertEquals(input2, result);
    }

    @Override
    protected Longest getInstance() {
        return new Longest();
    }

    @Override
    protected Class<? extends Longest> getFunctionClass() {
        return Longest.class;
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[]{ Object.class, Object.class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[]{ Object.class };
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final Longest function = new Longest();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "   \"class\" : \"uk.gov.gchq.koryphe.impl.function.Longest\"%n" +
                "}"), json);

        // When 2
        final Longest deserialised = JsonSerialiser.deserialise(json, Longest.class);

        // Then
        assertNotNull(deserialised);
    }
}
