package uk.gov.gchq.koryphe.tuple;

import uk.gov.gchq.koryphe.function.FunctionTest;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TupleInputAdapterTest extends FunctionTest<TupleInputAdapter> {
    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] { Tuple.class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] { Object.class };
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // todo serialisation test
    }

    @Override
    protected TupleInputAdapter getInstance() {
        return new TupleInputAdapter(new String[] { "input" });
    }

    @Override
    protected Iterable<TupleInputAdapter> getDifferentInstances() {
        return Arrays.asList(
                new TupleInputAdapter(),
                new TupleInputAdapter(new String[] { "differentInput" }),
                new TupleInputAdapter(new Integer[] { 1, 2, 3 })
        );
    }
}