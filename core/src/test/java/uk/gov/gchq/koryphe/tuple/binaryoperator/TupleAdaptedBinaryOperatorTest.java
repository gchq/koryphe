/*
 * Copyright 2017-2020 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.gchq.koryphe.tuple.binaryoperator;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.binaryoperator.BinaryOperatorTest;
import uk.gov.gchq.koryphe.binaryoperator.MockBinaryOperator;
import uk.gov.gchq.koryphe.impl.binaryoperator.Product;
import uk.gov.gchq.koryphe.impl.binaryoperator.Sum;
import uk.gov.gchq.koryphe.tuple.Tuple;
import uk.gov.gchq.koryphe.tuple.TupleInputAdapter;
import uk.gov.gchq.koryphe.tuple.TupleOutputAdapter;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TupleAdaptedBinaryOperatorTest extends BinaryOperatorTest<TupleAdaptedBinaryOperator> {

    @Test
    public void testTupleAggregation() {
        String[] inputs = new String[]{"input1", "input2", "input3"};
        String[] outputs = new String[]{"output1", "output2", "output3"};

        TupleAdaptedBinaryOperator<String, String> binaryOperator = new TupleAdaptedBinaryOperator<>();
        Tuple<String>[] tuples = new Tuple[]{mock(Tuple.class), mock(Tuple.class), mock(Tuple.class)};

        // set up the function
        BinaryOperator<String> function = mock(BinaryOperator.class);
        TupleInputAdapter<String, String> inputAdapter = mock(TupleInputAdapter.class);
        TupleOutputAdapter<String, String> outputAdapter = mock(TupleOutputAdapter.class);

        binaryOperator.setBinaryOperator(function);
        binaryOperator.setInputAdapter(inputAdapter);
        binaryOperator.setOutputAdapter(outputAdapter);

        Tuple<String> state = tuples[0];

        for (int i = 1; i < tuples.length; i++) {
            given(inputAdapter.apply(tuples[i])).willReturn(inputs[i]);
            given(inputAdapter.apply(state)).willReturn(outputs[i - 1]);
            given(function.apply(outputs[i - 1], inputs[i])).willReturn(outputs[i]);
            given(outputAdapter.apply(state, outputs[i])).willReturn(state);
            state = binaryOperator.apply(state, tuples[i]);
        }

        // check the expected calls
        for (int i = 1; i < tuples.length; i++) {
            verify(inputAdapter, times(1)).apply(tuples[i]);
            String in1 = outputs[i - 1];
            String in2 = inputs[i];
            verify(function, times(1)).apply(in1, in2);
            verify(inputAdapter, times(1)).apply(tuples[i]);
            verify(outputAdapter, times(1)).apply(tuples[0], outputs[i]);
        }
        verify(inputAdapter, times(2)).apply(tuples[0]);
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        TupleAdaptedBinaryOperator<String, Integer> binaryOperator = new TupleAdaptedBinaryOperator<>();
        MockBinaryOperator function = new MockBinaryOperator();
        TupleInputAdapter<String, Integer> inputAdapter = new TupleInputAdapter();
        binaryOperator.setInputAdapter(inputAdapter);
        binaryOperator.setBinaryOperator(function);

        String json = JsonSerialiser.serialise(binaryOperator);
        TupleAdaptedBinaryOperator<String, Integer> deserialisedBinaryOperator = JsonSerialiser.deserialise(json, TupleAdaptedBinaryOperator.class);

        // check deserialisation
        assertNotSame(binaryOperator, deserialisedBinaryOperator);

        BinaryOperator<Integer> deserialisedFunction = deserialisedBinaryOperator.getBinaryOperator();
        assertNotSame(function, deserialisedFunction);
        assertTrue(deserialisedFunction instanceof MockBinaryOperator);

        Function<Tuple<String>, Integer> deserialisedInputMask = deserialisedBinaryOperator.getInputAdapter();
        assertNotSame(inputAdapter, deserialisedInputMask);
        assertTrue(deserialisedInputMask instanceof Function);
    }

    @Override
    protected TupleAdaptedBinaryOperator getInstance() {
        return new TupleAdaptedBinaryOperator(new Sum(), new String[] { "a", "b"});
    }

    @Override
    protected Iterable<TupleAdaptedBinaryOperator> getDifferentInstances() {
        return Arrays.asList(
                new TupleAdaptedBinaryOperator(new Product(), new String[] { "a", "b"}),
                new TupleAdaptedBinaryOperator(new Sum(), new String[] { "c", "d"}),
                new TupleAdaptedBinaryOperator()
        );
    }
}
