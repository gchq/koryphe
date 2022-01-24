package uk.gov.gchq.koryphe.function;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.impl.function.MultiplyLongBy;
import uk.gov.gchq.koryphe.impl.function.StringSplit;
import uk.gov.gchq.koryphe.impl.function.ToLong;
import uk.gov.gchq.koryphe.impl.function.ToString;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FunctionCompositeTest extends FunctionTest<FunctionComposite>{
    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] { Object.class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] { Object.class };
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        FunctionComposite functionComposite = getInstance();
        String json =
                "{" +
                    "\"functions\": [" +
                        "{" +
                            "\"class\": \"uk.gov.gchq.koryphe.impl.function.ToLong\"" +
                        "}," +
                        "{" +
                            "\"class\": \"uk.gov.gchq.koryphe.impl.function.MultiplyLongBy\"," +
                            "\"by\": 10" +
                        "}" +
                    "]" +
                "}";

        // When
        String serialised = JsonSerialiser.serialise(functionComposite);
        FunctionComposite deserialised = JsonSerialiser.deserialise(json, FunctionComposite.class);

        // Then
        JsonSerialiser.assertEquals(json, serialised);
        assertEquals(functionComposite, deserialised);
    }

    @Override
    protected FunctionComposite getInstance() {
        return new FunctionComposite(Arrays.asList(
                new ToLong(),
                new MultiplyLongBy(10L)
        ));
    }

    @Override
    protected Iterable<FunctionComposite> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new FunctionComposite(),
                new FunctionComposite(Arrays.asList(
                        new ToString(),
                        new StringSplit(",")
                )),
                new FunctionComposite(Arrays.asList(
                        new ToLong(),
                        new MultiplyLongBy(100L)
                ))
        );
    }

    @Test
    public void shouldReturnInputIfNoComponentsAreProvided() {
        // Given
        FunctionComposite<String, String, Function> functionComposite = new FunctionComposite<>();

        // When
        String output = functionComposite.apply("test");

        // Then
        assertEquals("test", output);
    }

    @Test
    public void shouldApplyFunctionsInOrder() {
        // Given
        FunctionComposite<String, Long, Function> functionComposite = new FunctionComposite<>(Arrays.asList(
                new ToLong(),
                new MultiplyLongBy(100L)
        ));

        // When
        Long transformed = functionComposite.apply("4");

        // Then
        assertEquals(400L, transformed);
    }

    @Test
    public void shouldThrowExceptionIfInputsAndOutputsDontMatch() {
        // Given
        FunctionComposite<Integer, Long, Function> functionComposite = new FunctionComposite<>(Arrays.asList(
                new ToLong(),
                new StringSplit(" ")
        ));

        // When / Then
        ClassCastException e = assertThrows(ClassCastException.class, () -> functionComposite.apply(5));

        assertNotNull(e.getMessage());
    }
}