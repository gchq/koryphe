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
package uk.gov.gchq.koryphe.impl.function;

import org.apache.commons.io.Charsets;
import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ToStringTest extends FunctionTest<ToString> {

    @Test
    public void shouldReturnString() {
        // Given
        final ToString ts = new ToString();

        // When
        final String output = ts.apply("test string");

        // Then
        assertEquals("test string", output);
    }

    @Test
    public void shouldHandleArray() {
        // Given
        final ToString ts = new ToString();
        final String[] testArray = new String[] {"test", "string"};

        // When
        final String output = ts.apply(testArray);

        // Then
        assertEquals("[test, string]", output);
    }

    @Test
    public void shouldHandleByteArrayWithUtf8Charset() {
        // Given
        final ToString ts = new ToString(StandardCharsets.UTF_8);
        final byte[] bytes = "test string".getBytes(StandardCharsets.UTF_8);

        // When
        final String output = ts.apply(bytes);

        // Then
        assertEquals("test string", output);
    }

    @Test
    public void shouldHandleByteArrayWithUtf16Charset() {
        // Given
        final ToString ts = new ToString(StandardCharsets.UTF_16);
        final byte[] bytes = "test string".getBytes(StandardCharsets.UTF_16);

        // When
        final String output = ts.apply(bytes);

        // Then
        assertEquals("test string", output);
    }

    @Test
    public void shouldHandleNullObject() {
        // Given
        final ToString ts = new ToString();

        // When
        final String output = ts.apply(null);

        // Then
        assertNull(output);
    }

    @Override
    protected ToString getInstance() {
        return new ToString();
    }

    @Override
    protected Iterable<ToString> getDifferentInstances() {
        return Collections.singletonList(new ToString(Charsets.ISO_8859_1));
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
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final ToString ts = new ToString();

        // When
        final String json = JsonSerialiser.serialise(ts);

        // Then
        JsonSerialiser.assertEquals(String.format("{" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.ToString\"" +
                "}"), json);

        // When 2
        final ToString deserialisedTs = JsonSerialiser.deserialise(json, ToString.class);

        // Then 2
        assertNotNull(deserialisedTs);
    }
}
