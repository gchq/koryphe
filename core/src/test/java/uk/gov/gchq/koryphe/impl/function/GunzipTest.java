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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Function;
import java.util.zip.GZIPOutputStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GunzipTest extends FunctionTest {

    @Override
    protected Function getInstance() {
        return new Gunzip();
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return Gunzip.class;
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] { byte[].class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] { byte[].class };
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
        assertNotNull(deserialisedMethod);
    }

    @Test
    public void shouldUncompressString() throws IOException {
        // Given
        final Gunzip function = new Gunzip();
        final byte[] input = "test string".getBytes();
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
        assertArrayEquals(input, result);
    }

    @Test
    public void shouldReturnNullForNullInput() {
        // Given
        final Gunzip function = new Gunzip();

        // When
        final byte[] result = function.apply(null);

        // Then
        assertNull(result);
    }
}
