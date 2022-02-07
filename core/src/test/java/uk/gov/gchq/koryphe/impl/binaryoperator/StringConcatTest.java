/*
 * Copyright 2017-2022 Crown Copyright
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

package uk.gov.gchq.koryphe.impl.binaryoperator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.binaryoperator.BinaryOperatorTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StringConcatTest extends BinaryOperatorTest<StringConcat> {

    private String state;

    @BeforeEach
    public void before() {
        state = null;
    }

    @Test
    public void shouldConcatStringsTogether() {
        // Given
        final StringConcat function = new StringConcat();
        function.setSeparator(";");

        // When
        state = function.apply(state, "1");
        state = function.apply(state, "2");
        state = function.apply(state, null);

        // Then
        assertEquals("1;2", state);
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final StringConcat function = new StringConcat();

        // When 1
        final String json = JsonSerialiser.serialise(function);

        // Then 1
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.binaryoperator.StringConcat\",%n" +
                "  \"separator\" : \",\"%n" +
                "}"), json);

        // When 2
        final StringConcat deserialisedAggregator = JsonSerialiser.deserialise(json, StringConcat.class);

        // Then 2
        assertNotNull(deserialisedAggregator);
    }

    @Override
    protected StringConcat getInstance() {
        return new StringConcat();
    }

    @Override
    protected Iterable<StringConcat> getDifferentInstancesOrNull() {
        StringConcat alternative = new StringConcat();
        alternative.setSeparator(" ");
        return Collections.singletonList(alternative);
    }

}
