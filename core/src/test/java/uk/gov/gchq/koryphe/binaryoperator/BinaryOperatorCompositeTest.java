package uk.gov.gchq.koryphe.binaryoperator;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.impl.binaryoperator.Product;
import uk.gov.gchq.koryphe.impl.binaryoperator.Sum;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;


class BinaryOperatorCompositeTest extends BinaryOperatorTest<BinaryOperatorComposite> {
    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        BinaryOperatorComposite boc = getInstance();
        String json = "{" +
                    "\"operators\": [" +
                        "{" +
                            "\"class\": \"uk.gov.gchq.koryphe.impl.binaryoperator.Sum\"" +
                        "}," +
                        "{" +
                            "\"class\": \"uk.gov.gchq.koryphe.impl.binaryoperator.Product\"" +
                        "}" +
                    "]" +
                "}";

        // When
        String serialised = JsonSerialiser.serialise(boc);
        BinaryOperatorComposite deserialised = JsonSerialiser.deserialise(json, BinaryOperatorComposite.class);

        // Then
        JsonSerialiser.assertEquals(json, serialised);
        assertEquals(boc, deserialised);

    }

    @Override
    protected BinaryOperatorComposite getInstance() {
        return new BinaryOperatorComposite(Arrays.asList(
                new Sum(),
                new Product()
        ));
    }

    @Override
    protected Iterable<BinaryOperatorComposite> getDifferentInstances() {
        return Arrays.asList(
                new BinaryOperatorComposite(),
                new BinaryOperatorComposite(Arrays.asList(
                        new Product(),
                        new Sum()
                )),
                new BinaryOperatorComposite(Arrays.asList(
                        new Sum(),
                        new Sum()
                ))
        );
    }

    @Test
    public void shouldExecuteEachBinaryOperatorInOrder() {
        // Given
        BinaryOperatorComposite instance = getInstance(); // Sum then Product
        BinaryOperatorComposite reversed = new BinaryOperatorComposite(Arrays.asList(
                new Product(),
                new Sum()
        ));

        // When
        Object aggregated = instance.apply(5, 10);
        Object aggregatedInReverseOrder = reversed.apply(5, 10);

        // Then
        assertEquals(150, aggregated); // (5 + 10) x 10 = 150
        assertEquals(60, aggregatedInReverseOrder); // (5 x 10) + 10 = 60

    }
}