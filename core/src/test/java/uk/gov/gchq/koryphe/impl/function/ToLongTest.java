/*
 * Copyright 2018-2019 Crown Copyright
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

public class ToLongTest extends FunctionTest {
    @Test
    public void shouldConvertToLong() {
        // Given
        final ToLong function = new ToLong();

        // When
        Object output = function.apply(5);

        // Then
        assertEquals(new Long(5), output);
        assertEquals(Long.class, output.getClass());
    }

    @Override
    protected Function getInstance() {
        return new ToLong();
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return ToLong.class;
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final ToLong function = new ToLong();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.ToLong\"%n" +
                "}"), json);

        // When 2
        final ToLong deserialisedMethod = JsonSerialiser.deserialise(json, ToLong.class);

        // Then 2
        assertNotNull(deserialisedMethod);
    }
}
