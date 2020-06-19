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
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class StringRegexSplitTest extends FunctionTest {

    @Test
    public void shouldHandleNullInput() {
        // Given
        final StringRegexSplit function = new StringRegexSplit();

        // When
        final List<String> result = function.apply(null);

        // Then
        assertNull(result);
    }

    @Test
    public void shouldSplitString() {
        // Given
        final StringRegexSplit function = new StringRegexSplit(",");
        final String input = "first,second,third";

        // When
        final List<String> result = function.apply(input);

        // Then
        assertThat(result, hasItems("first", "second", "third"));
    }

    @Override
    protected StringRegexSplit getInstance() {
        return new StringRegexSplit();
    }

    @Override
    protected Class<? extends StringRegexSplit> getFunctionClass() {
        return StringRegexSplit.class;
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
        final StringRegexSplit function = new StringRegexSplit(",");

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.StringRegexSplit\",%n" +
                "  \"regex\" : \",\"%n" +
                "}"), json);

        // When 2
        final StringRegexSplit deserialisedMethod = JsonSerialiser.deserialise(json, StringRegexSplit.class);

        // Then 2
        assertNotNull(deserialisedMethod);
    }
}