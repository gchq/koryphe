/*
 * Copyright 2018-2020 Crown Copyright
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
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ToListTest extends FunctionTest<ToList> {

    @Test
    public void shouldConvertNullToList() {
        // Given
        final ToList function = new ToList();

        // When
        final Object result = function.apply(null);

        // Then
        assertThat(result)
                .isEqualTo(Lists.newArrayList((Object) null))
                .asList()
                .isNotNull()
                .isNotEmpty()
                .containsExactly((Object) null);
    }

    @Test
    public void shouldConvertStringToList() {
        // Given
        final ToList function = new ToList();
        final Object value = "value1";

        // When
        final Object result = function.apply(value);

        // Then
        assertThat(result)
                .isEqualTo(Lists.newArrayList(value))
                .isExactlyInstanceOf(ArrayList.class)
                .asList()
                .containsExactly(value);
    }

    @Test
    public void shouldConvertArrayToList() {
        // Given
        final ToList function = new ToList();
        final Object value = new String[] {"value1", "value2"};

        // When
        final Object result = function.apply(value);

        // Then
        assertThat(result)
                .isEqualTo(Lists.newArrayList((Object []) value))
                .isExactlyInstanceOf(ArrayList.class)
                .asList()
                .containsExactlyElementsOf(Lists.newArrayList((Object[]) value));
    }

    @Test
    public void shouldConvertSetToList() {
        // Given
        final ToList function = new ToList();
        final Set<String> value = Sets.newLinkedHashSet(Arrays.asList("value1", "value2"));

        // When
        final Object result = function.apply(value);

        // Then
        assertThat(result)
                .isEqualTo(Lists.newArrayList(value))
                .isExactlyInstanceOf(ArrayList.class)
                .asList()
                .containsExactlyElementsOf(value);
    }

    @Test
    public void shouldReturnAGivenList() {
        // Given
        final ToList function = new ToList();
        final Object value = Lists.newArrayList("value1", "value2");

        // When
        final Object result = function.apply(value);

        // Then
        assertThat(result).isSameAs(value);
    }

    @Override
    protected ToList getInstance() {
        return new ToList();
    }

    @Override
    protected Iterable<ToList> getDifferentInstancesOrNull() {
        return null;
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] {Object.class};
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] {List.class};
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final ToList function = new ToList();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.ToList\"%n" +
                "}"), json);

        // When 2
        final ToList deserialisedMethod = JsonSerialiser.deserialise(json, ToList.class);

        // Then 2
        assertThat(deserialisedMethod).isNotNull();
    }
}
