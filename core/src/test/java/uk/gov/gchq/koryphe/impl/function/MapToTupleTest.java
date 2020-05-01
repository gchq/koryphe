/*
 * Copyright 2020 Crown Copyright
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

package uk.gov.gchq.koryphe.impl.function;

import org.junit.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.tuple.MapTuple;
import uk.gov.gchq.koryphe.tuple.Tuple;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MapToTupleTest extends FunctionTest {

    @Test
    public void shouldConvertMapIntoMapTuple() {
        // Given
        final MapToTuple function = new MapToTuple();
        Map<String, Object> input = new HashMap<>();
        input.put("A", 1);
        input.put("B", 2);
        input.put("C", 3);

        // When
        Tuple output = function.apply(input);

        // Then
        assertEquals(new MapTuple<>(input), output);
    }

    @Override
    protected Function getInstance() {
        return new MapToTuple<String>();
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return MapToTuple.class;
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[]{Map.class};
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[]{Tuple.class};
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final MapToTuple function = new MapToTuple();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.MapToTuple\"" +
                "}"), json);

        // When 2
        final MapToTuple deserialisedMethod = JsonSerialiser.deserialise(json, MapToTuple.class);

        // Then 2
        assertNotNull(deserialisedMethod);
    }
}
