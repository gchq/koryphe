package uk.gov.gchq.koryphe.impl.function;/*
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

import org.junit.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.function.Function;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ToNullTest extends FunctionTest {
    @Test
    public void shouldReturnNull() {
        // Given
        final ToNull function = new ToNull();

        // When
        Object output = function.apply(new String("test"));

        // Then
        assertNull(output);
    }
    @Override
    protected Function getInstance() {
        return new ToNull();
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return ToNull.class;
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
        final ToNull function = new ToNull();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.ToNull\"%n" +
                "}"), json);

        // When 2
        final ToNull deserialisedMethod = JsonSerialiser.deserialise(json, ToNull.class);

        // Then 2
        assertNotNull(deserialisedMethod);
    }
}
