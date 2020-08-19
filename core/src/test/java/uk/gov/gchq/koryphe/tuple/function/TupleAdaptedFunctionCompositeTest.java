package uk.gov.gchq.koryphe.tuple.function;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.impl.function.MultiplyLongBy;
import uk.gov.gchq.koryphe.impl.function.ToLong;
import uk.gov.gchq.koryphe.impl.function.ToString;
import uk.gov.gchq.koryphe.tuple.Tuple;

import java.io.IOException;
import java.util.Arrays;

class TupleAdaptedFunctionCompositeTest extends FunctionTest<TupleAdaptedFunctionComposite> {

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
        // todo json serialisation test
    }

    @Override
    protected TupleAdaptedFunctionComposite<String> getInstance() {
        return new TupleAdaptedFunctionComposite.Builder<String>()
                .select(new String[] { "input" })
                .execute(new ToLong())
                .project(new String[] { "output"})
                .build();
    }

    @Override
    protected Iterable<TupleAdaptedFunctionComposite> getDifferentInstances() {
        return Arrays.asList(
                new TupleAdaptedFunctionComposite.Builder<String>()
                        .select(new String[] { "DifferentInput" })
                        .execute(new ToLong())
                        .project(new String[] { "output"})
                        .build(),
                new TupleAdaptedFunctionComposite.Builder<String>()
                        .select(new String[] { "input" })
                        .execute(new ToString())
                        .project(new String[] { "output"})
                        .build(),
                new TupleAdaptedFunctionComposite.Builder<String>()
                        .select(new String[] { "input" })
                        .execute(new ToLong())
                        .project(new String[] { "differentOutput"})
                        .build(),
                new TupleAdaptedFunctionComposite.Builder<Integer>()
                        .select(new Integer[] { 1 })
                        .execute(new ToLong())
                        .project(new Integer[] { 2 })
                        .build(),
                new TupleAdaptedFunctionComposite.Builder<String>()
                        .build(),
                new TupleAdaptedFunctionComposite.Builder<String>()
                        .select(new String[] { "input" })
                        .execute(new ToLong())
                        .project(new String[] { "midway"})
                        .select(new String[] { "midway"})
                        .execute(new MultiplyLongBy(10))
                        .project(new String[] { "output"})
                        .build()
        );
    }

    // todo normal tests
}