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

package uk.gov.gchq.koryphe.adapted;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.impl.function.Identity;
import uk.gov.gchq.koryphe.impl.function.MultiplyBy;
import uk.gov.gchq.koryphe.impl.function.ToLong;
import uk.gov.gchq.koryphe.util.EqualityTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StateAgnosticOutputAdapterTest extends EqualityTest<StateAgnosticOutputAdapter> {

    @Override
    protected StateAgnosticOutputAdapter getInstance() {
        return new StateAgnosticOutputAdapter(new Identity());
    }

    @Override
    protected Iterable<StateAgnosticOutputAdapter> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new StateAgnosticOutputAdapter(),
                new StateAgnosticOutputAdapter(new ToLong())
        );
    }

    @Test
    public void shouldReturnTheUnadaptedOutputIfNoAdapterIsProvided() {
        // Given
        StateAgnosticOutputAdapter<Object, Object, Object> soa = new StateAgnosticOutputAdapter<>();

        // When
        Object output = soa.apply(null, "input");

        // Then
        assertEquals("input", output);
    }

    @Test
    public void shouldApplyAnOutputAdapter() {
        // Given
        StateAgnosticOutputAdapter<Object, Integer, Integer> soa = new StateAgnosticOutputAdapter<>(new MultiplyBy(10));

        // When
        Object output = soa.apply(null, 10);

        // Then
        assertEquals(100, output);
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        StateAgnosticOutputAdapter<Object, Integer, Integer> soa = new StateAgnosticOutputAdapter<>(new MultiplyBy(10));
        String json =
                "{\n" +
                    "\"adapter\": {\n" +
                        "\"class\": \"uk.gov.gchq.koryphe.impl.function.MultiplyBy\",\n" +
                        "\"by\": 10\n" +
                    "}\n" +
                "}";
        // When
        String serialised = JsonSerialiser.serialise(soa);
        StateAgnosticOutputAdapter deserialised = JsonSerialiser.deserialise(json, StateAgnosticOutputAdapter.class);

        // Then
        JsonSerialiser.assertEquals(json, serialised);
        assertEquals(soa, deserialised);
    }
}