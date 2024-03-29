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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPOutputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class GunzipTest extends FunctionTest<Gunzip> {

    @Override
    protected Gunzip getInstance() {
        return new Gunzip();
    }

    @Override
    protected Iterable<Gunzip> getDifferentInstancesOrNull() {
        return null;
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[]{byte[].class};
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[]{byte[].class};
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final Gunzip function = new Gunzip();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.Gunzip\"" +
                "}"), json);

        // When 2
        final Gunzip deserialisedMethod = JsonSerialiser.deserialise(json, Gunzip.class);

        // Then 2
        assertThat(deserialisedMethod).isNotNull();
    }

    @Test
    public void shouldUncompressString() throws IOException {
        // Given
        final Gunzip function = new Gunzip();
        final byte[] input = "test string".getBytes(StandardCharsets.UTF_8);
        final byte[] gzip;
        try (final ByteArrayOutputStream out = new ByteArrayOutputStream();
             final GZIPOutputStream gzipOut = new GZIPOutputStream(out)) {
            gzipOut.write(input);
            gzipOut.flush();
            gzipOut.close();
            gzip = out.toByteArray();
        }

        // When
        final byte[] result = function.apply(gzip);

        // Then
        assertThat(result).isEqualTo(input);
    }

    @Test
    public void shouldReturnNullForNullInput() {
        // Given
        final Gunzip function = new Gunzip();

        // When
        final byte[] result = function.apply(null);

        // Then
        assertThat(result).isNull();
    }
}
