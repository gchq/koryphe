package uk.gov.gchq.koryphe.binaryoperator;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.impl.binaryoperator.Product;
import uk.gov.gchq.koryphe.impl.binaryoperator.StringConcat;
import uk.gov.gchq.koryphe.impl.binaryoperator.Sum;
import uk.gov.gchq.koryphe.impl.function.DivideBy;
import uk.gov.gchq.koryphe.impl.function.MultiplyBy;
import uk.gov.gchq.koryphe.impl.function.MultiplyLongBy;
import uk.gov.gchq.koryphe.impl.function.ToInteger;
import uk.gov.gchq.koryphe.impl.function.ToLong;
import uk.gov.gchq.koryphe.tuple.ArrayTuple;
import uk.gov.gchq.koryphe.tuple.TupleInputAdapter;
import uk.gov.gchq.koryphe.tuple.TupleOutputAdapter;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;


class AdaptedBinaryOperatorTest extends BinaryOperatorTest<AdaptedBinaryOperator> {
    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        AdaptedBinaryOperator adaptedBinaryOperator = new AdaptedBinaryOperator(new Product(), new ToLong(), new MultiplyBy(5));
        String json =
                "{\n" +
                        "\"binaryOperator\": {\n" +
                            "\"class\": \"uk.gov.gchq.koryphe.impl.binaryoperator.Product\"\n" +
                        "},\n" +
                        "\"inputAdapter\": {\n" +
                            "\"class\": \"uk.gov.gchq.koryphe.impl.function.ToLong\"\n" +
                        "},\n" +
                        "\"outputAdapter\": {\n" +
                            "\"class\": \"uk.gov.gchq.koryphe.adapted.StateAgnosticOutputAdapter\",\n" +
                            "\"adapter\": {\n" +
                                "\"class\": \"uk.gov.gchq.koryphe.impl.function.MultiplyBy\",\n" +
                                "\"by\": 5\n" +
                            "}\n" +
                        "}\n" +
                    "}";
        // When
        String serialised = JsonSerialiser.serialise(adaptedBinaryOperator);
        AdaptedBinaryOperator deserialised = JsonSerialiser.deserialise(json, AdaptedBinaryOperator.class);

        // Then
        JsonSerialiser.assertEquals(json, serialised);
        assertEquals(adaptedBinaryOperator, deserialised);
    }

    @Override
    protected AdaptedBinaryOperator getInstance() {
        return new AdaptedBinaryOperator(new Sum(), new ToLong(), new MultiplyBy(5));
    }

    @Override
    protected Iterable<AdaptedBinaryOperator> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new AdaptedBinaryOperator(new Product(), new ToLong(), new MultiplyBy(5)),
                new AdaptedBinaryOperator(new Sum(), new ToInteger(), new MultiplyBy(5)),
                new AdaptedBinaryOperator(new Sum(), new ToLong(), new DivideBy(5)),
                new AdaptedBinaryOperator()
        );
    }

    @Test
    public void IfNoOutputAdapterIsSpecifiedShouldReturnNewState() {
        // Given
        AdaptedBinaryOperator abo = new AdaptedBinaryOperator(new Product(), new ToLong(), (BinaryOperator<Number>) null);

        // When
        Object aggregated = abo.apply(2, 5);

        // then
        assertEquals(10L, aggregated);
    }

    @Test
    public void shouldAdaptOutputIfOutputAdapterIsSpecified() {
        // Given
        AdaptedBinaryOperator abo = new AdaptedBinaryOperator(new Product(), new ToLong(), new MultiplyLongBy(5L));

        // When
        Object aggregated = abo.apply(2, 5);

        // then
        assertEquals(50L, aggregated);
    }

    @Test
    public void shouldConsiderStateWhenOutputAdapterIsBiFunction() {
        // Given
        ArrayTuple state = new ArrayTuple();
        state.put(0, "tuple");

        ArrayTuple input = new ArrayTuple();
        state.put(0, "test");;

        TupleInputAdapter<Integer, String> inputAdapter = new TupleInputAdapter<>();
        inputAdapter.setSelection(new Integer[] { 0 });

        TupleOutputAdapter<Integer, String> outputAdapter = new TupleOutputAdapter<>();
        outputAdapter.setProjection(new Integer[] { 0 });

        AdaptedBinaryOperator abo = new AdaptedBinaryOperator(new StringConcat(" "), inputAdapter, outputAdapter);

        // When
        Object aggregated = abo.apply(state, input);

        // Then
        ArrayTuple expected = new ArrayTuple();
        state.put(0, "tuple test");

        assertEquals(expected, aggregated);
    }

    @Test
    public void shouldThrowExceptionIfNoBinaryOperatorIsSpecified() {
        // Given
        AdaptedBinaryOperator abo = new AdaptedBinaryOperator();

        // When / Then

        try {
            abo.apply("will", "fail");
            fail("Expected an exception");
        } catch (Exception e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void shouldPassInputDirectlyToBinaryOperatorIfNoInputAdapterIsSpecified() {
        // Given
        AdaptedBinaryOperator abo = new AdaptedBinaryOperator(new Product(), null, new MultiplyBy(10));

        // When
        Object aggregated = abo.apply(2, 5);

        // then
        assertEquals(Integer.class, aggregated.getClass());
        assertEquals(100, aggregated);
    }

    @Test
    public void shouldJustDelegateToBinaryOperatorIfNoAdaptersAreSpecified() {
        // Given
        AdaptedBinaryOperator abo = new AdaptedBinaryOperator(new Product(), null, (BiFunction) null);

        // When
        Object aggregated = abo.apply(2, 5);

        // then
        assertEquals(Integer.class, aggregated.getClass());
        assertEquals(10, aggregated);
    }


}