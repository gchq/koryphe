package uk.gov.gchq.koryphe.tuple.binaryoperator;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.binaryoperator.BinaryOperatorTest;
import uk.gov.gchq.koryphe.impl.binaryoperator.Max;
import uk.gov.gchq.koryphe.impl.binaryoperator.Product;
import uk.gov.gchq.koryphe.impl.binaryoperator.Sum;
import uk.gov.gchq.koryphe.tuple.ArrayTuple;
import uk.gov.gchq.koryphe.tuple.Tuple;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class TupleAdaptedBinaryOperatorCompositeTest extends BinaryOperatorTest<TupleAdaptedBinaryOperatorComposite> {
    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        TupleAdaptedBinaryOperatorComposite instance = getInstance();
        String json = "" +
                "{" +
                    "\"operators\": [" +
                        "{" +
                            "\"selection\": [\"input\", \"anotherInput\"]," +
                            "\"binaryOperator\": {" +
                                "\"class\": \"uk.gov.gchq.koryphe.impl.binaryoperator.Sum\"" +
                            "}" +
                        "}" +
                    "]" +
                "}";

        // When
        String serialised = JsonSerialiser.serialise(instance);
        TupleAdaptedBinaryOperatorComposite deserialised = JsonSerialiser.deserialise(json, TupleAdaptedBinaryOperatorComposite.class);

        // Then
        JsonSerialiser.assertEquals(json, serialised);
        assertEquals(instance, deserialised);

    }

    @Override
    protected TupleAdaptedBinaryOperatorComposite getInstance() {
        return new TupleAdaptedBinaryOperatorComposite.Builder()
                .select(new String[] { "input", "anotherInput" })
                .execute(new Sum())
                .build();
    }

    @Override
    protected Iterable<TupleAdaptedBinaryOperatorComposite> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new TupleAdaptedBinaryOperatorComposite.Builder()
                        .select(new String[] { "differentInput", "anotherInput" })
                        .execute(new Sum())
                        .build(),
                new TupleAdaptedBinaryOperatorComposite.Builder()
                        .select(new String[] { "input", "anotherInput" })
                        .execute(new Product())
                        .build(),
                new TupleAdaptedBinaryOperatorComposite(),
                new TupleAdaptedBinaryOperatorComposite.Builder()
                        .select(new String[] { "input", "anotherInput" })
                        .execute(new Sum())
                        .select(new String[] { "input", "differentInput"})
                        .execute(new Max())
                .build()
        );
    }

    @Test
    public void shouldErrorIfObjectsAreTheWrongType() {
        // Given
        ArrayTuple stateTuple = new ArrayTuple(5, 10, 15);
        ArrayTuple inputTuple = new ArrayTuple(1, "two", 3);

        // When
        TupleAdaptedBinaryOperatorComposite<Integer> boc = new TupleAdaptedBinaryOperatorComposite.Builder<Integer>()
                .select(new Integer[]{ 0 })
                .execute(new Product())
                .select(new Integer[] { 1 })
                .execute(new Max())
                .select(new Integer[] { 2 })
                .execute(new Sum())
                .build();

        // Then
        try {
            boc.apply(stateTuple, inputTuple);
            fail("Expected aggregation to fail");
        } catch (ClassCastException e) {
            assertNotNull(e.getMessage());
        }

    }

    @Test
    public void shouldMergeTheInputTupleIntoTheStateTuple() {
        // Given
        ArrayTuple stateTuple = new ArrayTuple(5, 10, 15);
        ArrayTuple inputTuple = new ArrayTuple(1, 2, 3);

        // When
        TupleAdaptedBinaryOperatorComposite<Integer> boc = new TupleAdaptedBinaryOperatorComposite.Builder<Integer>()
                .select(new Integer[]{ 0 })
                .execute(new Product())
                .select(new Integer[] { 1 })
                .execute(new Max())
                .select(new Integer[] { 2 })
                .execute(new Sum())
                .build();

        Tuple<Integer> agg = boc.apply(stateTuple, inputTuple);

        // Then
        assertEquals(5, agg.get(0));
        assertEquals(10, agg.get(1));
        assertEquals(18, agg.get(2));
    }


}