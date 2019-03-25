/*
 * Copyright 2017-2019 Crown Copyright
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
import static org.junit.Assert.assertNull;

public class ToStringTest extends FunctionTest {

    @Test
    public void shouldReturnString() {
        // Given
        final ToString ts = new ToString();

        // When
        String output = ts.apply("test string");

        // Then
        assertEquals("test string", output);
    }

    @Test
    public void shouldHandleArray() {
        // Given
        final ToString ts = new ToString();
        final String[] testArray = new String[]{"test", "string"};

        // When
        String output = ts.apply(testArray);

        // Then
        assertEquals("[test, string]", output);
    }

    @Test
    public void shouldHandleNullObject() {
        // Given
        final ToString ts = new ToString();

        // When
        String output = ts.apply(null);

        // Then
        assertNull(output);
    }

    @Override
    protected Function getInstance() {
        return new ToString();
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return ToString.class;
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
