/*
 * Copyright 2020-2022 Crown Copyright
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

package uk.gov.gchq.koryphe.tuple.function;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.impl.function.ParseDate;
import uk.gov.gchq.koryphe.impl.function.ToLong;
import uk.gov.gchq.koryphe.impl.function.ToUpperCase;
import uk.gov.gchq.koryphe.tuple.ArrayTuple;
import uk.gov.gchq.koryphe.tuple.MapTuple;
import uk.gov.gchq.koryphe.tuple.Tuple;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class TupleAdaptedFunctionTest extends FunctionTest<TupleAdaptedFunction> {

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] { Tuple.class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] { Tuple.class };
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        TupleAdaptedFunction instance = getInstance();
        String json = "" +
                "{" +
                    "\"selection\": [ \"input\" ]," +
                    "\"function\": {" +
                        "\"class\": \"uk.gov.gchq.koryphe.impl.function.ToUpperCase\"" +
                    "}," +
                    "\"projection\": [ \"output\" ]" +
                "}";

        // When
        String serialised = JsonSerialiser.serialise(instance);
        TupleAdaptedFunction deserialised = JsonSerialiser.deserialise(json, TupleAdaptedFunction.class);

        // Then
        JsonSerialiser.assertEquals(json, serialised);
        assertThat(deserialised).isEqualTo(instance);
    }

    @Override
    protected TupleAdaptedFunction getInstance() {
        return new TupleAdaptedFunction(new String[] {"input"}, new ToUpperCase(), new String[] { "output" });
    }

    @Override
    protected Iterable<TupleAdaptedFunction> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new TupleAdaptedFunction(new String[] {"differentInput"}, new ToUpperCase(), new String[] { "output" }),
                new TupleAdaptedFunction(new String[] {"input"}, new ToUpperCase(), new String[] { "DifferentOutput" }),
                new TupleAdaptedFunction(new String[] {"input"}, new ParseDate(), new String[] { "output" }),
                new TupleAdaptedFunction()
        );
    }

    @Test
    public void shouldErrorIfInputIsTheWrongType() {
        // Given
        TupleAdaptedFunction<String, Object, Long> function = new TupleAdaptedFunction<>(new String[]{"input"}, new ToLong(), new String[]{"output"});
        MapTuple<String> inputs = new MapTuple<>();
        inputs.put("input", "aString");

        // When / Then
        assertThatExceptionOfType(NumberFormatException.class).isThrownBy(() -> function.apply(inputs));
    }

    @Test
    public void shouldNotRemoveTheInputFromTheTuple() {
        // Given
        TupleAdaptedFunction<Integer, Object, String> instance = new TupleAdaptedFunction<>(new Integer[]{0}, new ToUpperCase(), new Integer[]{1});
        ArrayTuple objects = new ArrayTuple("test", null);

        // When
        ArrayTuple returnedTuple = (ArrayTuple) instance.apply(objects);

        // Then
        assertThat(returnedTuple.get(0)).isEqualTo("test");
        assertThat(returnedTuple.get(1)).isEqualTo("TEST");
    }
}
