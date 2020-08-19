package uk.gov.gchq.koryphe.tuple.function;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.impl.function.ParseDate;
import uk.gov.gchq.koryphe.impl.function.ToUpperCase;
import uk.gov.gchq.koryphe.tuple.Tuple;

import java.io.IOException;
import java.util.Arrays;

class TupleAdaptedFunctionTest extends FunctionTest<TupleAdaptedFunction> {

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] { Tuple.class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] { Tuple.class };
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // todo serialisation
    }

    @Override
    protected TupleAdaptedFunction getInstance() {
        return new TupleAdaptedFunction(new String[] {"input"}, new ToUpperCase(), new String[] { "output" });
    }

    @Override
    protected Iterable<TupleAdaptedFunction> getDifferentInstances() {
        return Arrays.asList(
                new TupleAdaptedFunction(new String[] {"differentInput"}, new ToUpperCase(), new String[] { "output" }),
                new TupleAdaptedFunction(new String[] {"input"}, new ToUpperCase(), new String[] { "DifferentOutput" }),
                new TupleAdaptedFunction(new String[] {"input"}, new ParseDate(), new String[] { "output" }),
                new TupleAdaptedFunction()
        );
    }

    // todo actual tests
}