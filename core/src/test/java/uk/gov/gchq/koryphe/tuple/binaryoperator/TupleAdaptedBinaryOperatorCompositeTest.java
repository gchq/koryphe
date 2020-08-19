package uk.gov.gchq.koryphe.tuple.binaryoperator;

import uk.gov.gchq.koryphe.binaryoperator.BinaryOperatorTest;
import uk.gov.gchq.koryphe.impl.binaryoperator.Product;
import uk.gov.gchq.koryphe.impl.binaryoperator.Sum;

import java.io.IOException;
import java.util.Arrays;

public class TupleAdaptedBinaryOperatorCompositeTest extends BinaryOperatorTest<TupleAdaptedBinaryOperatorComposite> {

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // todo json serialisation test
    }

    @Override
    protected TupleAdaptedBinaryOperatorComposite getInstance() {
        return new TupleAdaptedBinaryOperatorComposite.Builder()
                .select(new String[] { "input", "anotherInput" })
                .execute(new Sum())
                .build();
    }

    @Override
    protected Iterable<TupleAdaptedBinaryOperatorComposite> getDifferentInstances() {
        return Arrays.asList(
                new TupleAdaptedBinaryOperatorComposite.Builder()
                        .select(new String[] { "differentInput", "anotherInput" })
                        .execute(new Sum())
                        .build(),
                new TupleAdaptedBinaryOperatorComposite.Builder()
                        .select(new String[] { "input", "anotherInput" })
                        .execute(new Product())
                        .build(),
                new TupleAdaptedBinaryOperatorComposite()
        );
    }

    // todo testing
}