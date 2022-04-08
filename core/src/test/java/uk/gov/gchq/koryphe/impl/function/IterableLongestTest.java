/*
 * Copyright 2020-2022 Crown Copyright
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

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class IterableLongestTest extends FunctionTest<IterableLongest> {

    @Test
    public void shouldHandleNullInput() {
        // Given
        final IterableLongest function = getInstance();

        // When
        final Object result = function.apply(null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    public void shouldGetLongestItemFromList() {
        // Given
        final IterableLongest function = getInstance();
        final List<String> list = Lists.newArrayList("a", "ab", "abc");

        // When
        final Object result = function.apply(list);

        // Then
        assertThat(result).isEqualTo("abc");
    }

    @Override
    protected IterableLongest getInstance() {
        return new IterableLongest();
    }

    @Override
    protected Iterable<IterableLongest> getDifferentInstancesOrNull() {
        return null;
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[]{ Iterable.class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[]{ Object.class };
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final IterableLongest function = new IterableLongest();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "   \"class\" : \"uk.gov.gchq.koryphe.impl.function.IterableLongest\"%n" +
                "}"), json);

        // When 2
        final IterableLongest deserialised = JsonSerialiser.deserialise(json, IterableLongest.class);

        // Then
        assertThat(deserialised).isNotNull();
    }
}
