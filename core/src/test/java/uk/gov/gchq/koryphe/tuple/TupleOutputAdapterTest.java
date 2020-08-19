package uk.gov.gchq.koryphe.tuple;

import uk.gov.gchq.koryphe.util.EqualityTest;

import java.io.IOException;
import java.util.Arrays;

class TupleOutputAdapterTest extends EqualityTest<TupleOutputAdapter> { // Can't extend FunctionTest as TupleOutputAdapter is a BiFunction rather than a Function.

    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // todo
    }

    @Override
    protected TupleOutputAdapter getInstance() {
        return new TupleOutputAdapter(new String[] { "input" });
    }

    @Override
    protected Iterable<TupleOutputAdapter> getDifferentInstances() {
        return Arrays.asList(
                new TupleOutputAdapter(new String[] { "Different" }),
                new TupleOutputAdapter(new Integer[] { 1, 2, 3 }),
                new TupleOutputAdapter()
        );
    }

    // todo actual testing
}