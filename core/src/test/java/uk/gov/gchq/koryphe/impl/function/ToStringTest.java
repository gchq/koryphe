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

package uk.gov.gchq.koryphe.impl.function;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class ToStringTest extends FunctionTest<ToString> {

    @Test
    public void shouldReturnString() {
        // Given
        final ToString function = new ToString();

        // When
        final String output = function.apply("test string");

        // Then
        assertThat(output).isEqualTo("test string");
    }

    @Test
    public void shouldHandleArray() {
        // Given
        final ToString function = new ToString();
        final String[] testArray = new String[] {"test", "string"};

        // When
        final String output = function.apply(testArray);

        // Then
        assertThat(output).isEqualTo("[test, string]");
    }

    @Test
    public void shouldHandleByteArrayWithUtf8Charset() {
        // Given
        final ToString function = new ToString(StandardCharsets.UTF_8);
        final byte[] bytes = "test string".getBytes(StandardCharsets.UTF_8);

        // When
        final String output = function.apply(bytes);

        // Then
        assertThat(output).isEqualTo("test string");
    }

    @Test
    public void shouldHandleByteArrayWithUtf16Charset() {
        // Given
        final ToString function = new ToString(StandardCharsets.UTF_16);
        final byte[] bytes = "test string".getBytes(StandardCharsets.UTF_16);

        // When
        final String output = function.apply(bytes);

        // Then
        assertThat(output).isEqualTo("test string");
    }

    @Test
    public void shouldHandleNullObject() {
        // Given
        final ToString function = new ToString();

        // When
        final String output = function.apply(null);

        // Then
        assertThat(output).isNull();
    }

    @Override
    protected ToString getInstance() {
        return new ToString();
    }

    @Override
    protected Iterable<ToString> getDifferentInstancesOrNull() {
        return Collections.singletonList(new ToString(StandardCharsets.ISO_8859_1));
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
        final ToString function = new ToString();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals("{" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.ToString\"" +
                "}", json);

        // When 2
        final ToString deserialisedMethod = JsonSerialiser.deserialise(json, ToString.class);

        // Then 2
        assertThat(deserialisedMethod).isNotNull();
    }
}
