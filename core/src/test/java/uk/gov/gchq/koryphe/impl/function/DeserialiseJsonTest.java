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
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.assertj.core.api.InstanceOfAssertFactories.MAP;


public class DeserialiseJsonTest extends FunctionTest<DeserialiseJson> {
    @Override
    protected DeserialiseJson getInstance() {
        return new DeserialiseJson();
    }

    @Override
    protected Iterable<DeserialiseJson> getDifferentInstancesOrNull() {
        return Collections.singletonList(new DeserialiseJson(Long.class));
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] {String.class};
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] {Object.class};
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final DeserialiseJson function = new DeserialiseJson().outputClass(Map.class);

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "   \"class\" : \"uk.gov.gchq.koryphe.impl.function.DeserialiseJson\",%n" +
                "   \"outputClass\" : \"java.util.Map\"" +
                "}"), json);
    }

    @Test
    public void shouldParseJson() {
        // Given
        final DeserialiseJson function = new DeserialiseJson();
        final String input = "{\"elements\": [{\"value\": \"value1\"}, {\"value\": \"value2\"}]}";

        // When
        Object result = function.apply(input);

        // Then
        assertThat(result)
                .asInstanceOf(MAP)
                .hasSize(1)
                .extracting("elements", as(LIST))
                .hasSize(2)
                .extracting("value")
                .containsExactly("value1", "value2");
    }

    @Test
    public void shouldReturnNullForNullInput() {
        // Given
        final DeserialiseJson function = new DeserialiseJson();

        // When
        Object result = function.apply(null);

        // Then
        assertThat(result).isNull();
    }
}
