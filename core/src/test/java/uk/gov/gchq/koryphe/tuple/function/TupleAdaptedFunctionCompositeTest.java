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
import uk.gov.gchq.koryphe.impl.function.MultiplyLongBy;
import uk.gov.gchq.koryphe.impl.function.ToLong;
import uk.gov.gchq.koryphe.impl.function.ToString;
import uk.gov.gchq.koryphe.tuple.MapTuple;
import uk.gov.gchq.koryphe.tuple.Tuple;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

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
    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        TupleAdaptedFunctionComposite<String> instance = getInstance();
        String json = "" +
                "{" +
                    "\"functions\": [" +
                        "{" +
                            "\"selection\": [ \"input\" ]," +
                            "\"function\": {" +
                                "\"class\": \"uk.gov.gchq.koryphe.impl.function.ToLong\"" +
                            "}," +
                            "\"projection\": [ \"midway\" ]" +
                        "}," +
                        "{" +
                            "\"selection\": [ \"midway\" ]," +
                            "\"function\": {" +
                                "\"class\": \"uk.gov.gchq.koryphe.impl.function.MultiplyLongBy\"," +
                                "\"by\": 10" +
                            "}," +
                            "\"projection\": [ \"output\" ]" +
                        "}" +
                    "]" +
                "}";
        // When
        String serialised = JsonSerialiser.serialise(instance);
        TupleAdaptedFunctionComposite deserialised = JsonSerialiser.deserialise(json, TupleAdaptedFunctionComposite.class);

        // Then
        JsonSerialiser.assertEquals(json, serialised);
        assertEquals(instance, deserialised);
    }

    @Override
    protected TupleAdaptedFunctionComposite<String> getInstance() {
        return new TupleAdaptedFunctionComposite.Builder<String>()
                .select(new String[] { "input" })
                .execute(new ToLong())
                .project(new String[] { "midway"})
                .select(new String[] { "midway"})
                .execute(new MultiplyLongBy(10))
                .project(new String[] { "output"})
                .build();
    }

    @Override
    protected Iterable<TupleAdaptedFunctionComposite> getDifferentInstancesOrNull() {
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
                        .project(new String[] { "output"})
                        .build()

        );
    }

    @Test
    public void shouldThrowExceptionIfInputIsWrongType() {
        // Given
        TupleAdaptedFunctionComposite<String> instance = getInstance();
        MapTuple<String> inputTuple = new MapTuple<>();
        inputTuple.put("input", "aString");

        // When / Then
        try {
            instance.apply(inputTuple);
            fail("Expected Function to fail as the input should be a number but was a String");
        } catch (NumberFormatException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void shouldSelectApplyAndProjectInOrder() {
        // Given
        TupleAdaptedFunctionComposite<String> instance = getInstance();
        MapTuple<String> inputTuple = new MapTuple<>();
        inputTuple.put("input", 3);

        // When
        Tuple<String> transformed = instance.apply(inputTuple);

        // Then
        assertEquals(30L, transformed.get("output"));

    }
}