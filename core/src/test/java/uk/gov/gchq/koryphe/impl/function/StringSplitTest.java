/*
 * Copyright 2020 Crown Copyright
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
import java.util.List;
import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class StringSplitTest extends FunctionTest {

    @Test
    public void shouldHandleNullInput() {
        // Given
        final StringSplit function = new StringSplit();

        // When
        final List<String> result = function.apply(null);

        // Then
        assertNull(result);
    }

    @Test
    public void shouldHandleNullDelimiter() {
        // Given
        final StringSplit function = new StringSplit(null);
        final String input = "first,second,third";

        // When
        final List<String> result = function.apply(input);

        // Then
        assertThat(result, hasItems("first,second,third"));
        assertThat(result, hasSize(1));
    }

    @Test
    public void shouldSplitString() {
        // Given
        final StringSplit function = new StringSplit(",");
        final String input = "first,second,third";

        // When
        final List<String> result = function.apply(input);

        // Then
        assertThat(result, hasItems("first", "second", "third"));
    }

    @Test
    public void shouldSplitEmptyString() {
        // Given
        final StringSplit function = new StringSplit(",");
        final String input = "";

        // When
        final List<String> result = function.apply(input);

        // Then
        assertThat(result, is(empty()));
    }

    @Override
    protected StringSplit getInstance() {
        return new StringSplit();
    }

    @Override
    protected Iterable<Function> getDifferentInstances() {
        return Collections.singletonList(new StringSplit(" "));
    }

    @Override
    protected Class<? extends StringSplit> getFunctionClass() {
        return StringSplit.class;
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[]{ String.class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[]{ List.class };
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final StringSplit function = new StringSplit(",");

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.StringSplit\",%n" +
                "  \"delimiter\" : \",\"%n" +
                "}"), json);

        // When 2
        final StringSplit deserialisedMethod = JsonSerialiser.deserialise(json, StringSplit.class);

        // Then 2
        assertNotNull(deserialisedMethod);
    }
}