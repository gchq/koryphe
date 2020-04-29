/*
 * Copyright 2018-2020 Crown Copyright
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
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SetValueTest extends FunctionTest {
    private static final String SET_VALUE = "testVal";

    @Test
    public void shouldSetValue() {
        // Given
        final SetValue function = new SetValue(SET_VALUE);

        // When
        Object output = function.apply("test");

        // Then
        assertEquals(SET_VALUE, output);
    }

    @Override
    protected Function getInstance() {
        return new SetValue(SET_VALUE);
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return SetValue.class;
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] { Object.class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] { Object.class };
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final SetValue function = new SetValue(SET_VALUE);

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.SetValue\",%n" +
                "  \"value\" : \"testVal\"%n" +
                "}"), json);

        // When 2
        final SetValue deserialisedMethod = JsonSerialiser.deserialise(json, SetValue.class);

        // Then 2
        assertNotNull(deserialisedMethod);
    }

    @Test
    public void shouldSerialiseAndDeserialiseLong() throws IOException {
        // Given
        final SetValue function = new SetValue(1L);

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.SetValue\",%n" +
                "  \"value\" : {\"java.lang.Long\" : 1}%n" +
                "}"), json);

        // When 2
        final SetValue deserialisedFunction = JsonSerialiser.deserialise(json, SetValue.class);

        // Then 2
        assertEquals(1L, deserialisedFunction.getValue());
    }
}
