/*
 * Copyright 2019-2020 Crown Copyright
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

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ToBytesTest extends FunctionTest<ToBytes> {

    @Test
    public void shouldGetBytes() {
        // Given
        final ToBytes ts = new ToBytes(StandardCharsets.UTF_16);

        // When
        final byte[] output = ts.apply("test string");

        // Then
        assertArrayEquals("test string".getBytes(StandardCharsets.UTF_16), output);
    }

    @Test
    public void shouldHandleNullObject() {
        // Given
        final ToBytes ts = new ToBytes();

        // When
        final byte[] output = ts.apply(null);

        // Then
        assertNull(output);
    }

    @Override
    protected ToBytes getInstance() {
        return new ToBytes();
    }

    @Override
    protected Iterable<ToBytes> getDifferentInstances() {
        return Collections.singletonList(new ToBytes(StandardCharsets.US_ASCII));
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] {String.class};
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] {byte[].class};
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final ToBytes ts = new ToBytes();

        // When
        final String json = JsonSerialiser.serialise(ts);

        // Then
        JsonSerialiser.assertEquals(String.format("{" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.ToBytes\"" +
                "}"), json);

        // When 2
        final ToBytes deserialisedTs = JsonSerialiser.deserialise(json, ToBytes.class);

        // Then 2
        assertNotNull(deserialisedTs);
    }
}
