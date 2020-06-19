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

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ToLowerCaseTest extends FunctionTest {

    private static final String TEST_STRING = "TEST STRING";

    @Test
    public void shouldLowerCaseInputString() {
        // Given
        final Function function = getInstance();

        // When
        final Object output = function.apply(TEST_STRING);

        // Then
        assertEquals(StringUtils.lowerCase(TEST_STRING), output);
    }

    @Test
    public void shouldLowerCaseInputObject() {
        // Given
        final Function function = getInstance();
        final ToLowerCaseTestObject input = new ToLowerCaseTestObject();

        // When
        final Object output = function.apply(input);

        // Then
        assertEquals(StringUtils.lowerCase(input.getClass().getSimpleName().toUpperCase()), output);
    }

    @Test
    public void shouldHandleNullInput() {
        // Given
        final Function function = getInstance();

        // When
        Object output = function.apply(null);

        // Then
        assertNull(output);
    }

    @Override
    protected Function getInstance() {
        return new ToLowerCase();
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return ToLowerCase.class;
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] {Object.class};
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] {String.class};
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final ToLowerCase function = new ToLowerCase();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.ToLowerCase\"%n" +
                "}"), json);

        // When 2
        final ToLowerCase deserialisedMethod = JsonSerialiser.deserialise(json, ToLowerCase.class);

        // Then 2
        assertNotNull(deserialisedMethod);
    }

    public final static class ToLowerCaseTestObject {

        @Override
        public String toString() {
            return this.getClass().getSimpleName().toUpperCase();
        }
    }
}
