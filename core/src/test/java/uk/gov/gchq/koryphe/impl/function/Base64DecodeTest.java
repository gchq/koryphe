/*
 * Copyright 2019 Crown Copyright
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

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class Base64DecodeTest extends FunctionTest {
    @Override
    protected Function getInstance() {
        return new Base64Decode();
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return Base64Decode.class;
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final Base64Decode function = new Base64Decode("UTF-16");

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.Base64Decode\",%n" +
                "  \"charset\" : \"UTF-16\"%n" +
                "}"), json);

        // When 2
        final Base64Decode deserialisedMethod = JsonSerialiser.deserialise(json, Base64Decode.class);

        // Then 2
        assertNotNull(deserialisedMethod);
    }

    @Test
    public void shouldDecodeBase64String() {
        // Given
        final Base64Decode function = new Base64Decode("UTF-8");
        String input = "test string";
        final String base64String = Base64.encodeBase64String(input.getBytes(StandardCharsets.UTF_8));

        // When
        final String result = function.apply(base64String);

        // Then
        assertEquals(input, result);
    }

    @Test
    public void shouldReturnNullForNullInput() {
        // Given
        final Base64Decode function = new Base64Decode("UTF-8");
        String input = null;

        // When
        final String result = function.apply(input);

        // Then
        assertNull(result);
    }
}
