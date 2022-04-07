/*
 * Copyright 2019-2022 Crown Copyright
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

import static org.assertj.core.api.Assertions.assertThat;

public class ToBytesTest extends FunctionTest<ToBytes> {

    @Test
    public void shouldGetBytes() {
        // Given
        final ToBytes function = new ToBytes(StandardCharsets.UTF_16);

        // When
        final byte[] output = function.apply("test string");

        // Then
        assertThat(output).isEqualTo("test string".getBytes(StandardCharsets.UTF_16));
    }

    @Test
    public void shouldHandleNullObject() {
        // Given
        final ToBytes function = new ToBytes();

        // When
        final byte[] output = function.apply(null);

        // Then
        assertThat(output).isNull();
    }

    @Override
    protected ToBytes getInstance() {
        return new ToBytes();
    }

    @Override
    protected Iterable<ToBytes> getDifferentInstancesOrNull() {
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
        final ToBytes function = new ToBytes();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals("{" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.ToBytes\"" +
                "}", json);

        // When 2
        final ToBytes deserialisedMethod = JsonSerialiser.deserialise(json, ToBytes.class);

        // Then 2
        assertThat(deserialisedMethod).isNotNull();
    }
}
