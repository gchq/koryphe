/*
 * Copyright 2017-2022 Crown Copyright
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

package uk.gov.gchq.koryphe.impl.predicate;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.predicate.PredicateTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class StringContainsTest extends PredicateTest<StringContains> {

    private static final String INPUT = "This is a test string, used for the StringContains test";

    @Test
    public void shouldAcceptWhenStringInValue() {
        // Given
        final StringContains filter = new StringContains("used");

        // When / Then
        assertThat(filter).accepts(INPUT);
    }

    @Test
    public void shouldRejectMismatchedCases() {
        // Given
        final StringContains filter = new StringContains("stringcontains");

        // When / Then
        assertThat(filter).rejects(INPUT);
    }

    @Test
    public void shouldAcceptEmptyString() {
        // Given
        final StringContains filter = new StringContains("");

        // When / Then
        assertThat(filter).accepts(INPUT);
    }

    @Test
    public void shouldRejectNullValue() {
        // Given
        final StringContains filter = new StringContains(null);

        // When / Then
        assertThat(filter).rejects(INPUT);
    }

    @Test
    public void shouldRejectNullInput() {
        // Given
        final StringContains filter = new StringContains("test");

        // When / Then
        assertThat(filter).rejects((String) null);
    }

    @Test
    public void shouldMatchWhenIgnoreCaseSet() {
        // Given
        final StringContains filter = new StringContains("stringcontains");
        filter.setIgnoreCase(true);

        // When / Then
        assertThat(filter).accepts(INPUT);
    }

    @Override
    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final String value = "stringcontains";
        final StringContains filter = new StringContains(value);

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" + "  \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.StringContains\",%n" +
                                                          "  \"value\" : \"stringcontains\",%n" +
                                                          "  \"ignoreCase\" : false%n" +
                                                          "}"), json);

        // When 2
        final StringContains deserialisedFilter = JsonSerialiser.deserialise(json, StringContains.class);

        // Then 2
        assertThat(deserialisedFilter).isNotNull();
        assertThat(deserialisedFilter.getValue()).isEqualTo(value);
    }

    @Override
    protected StringContains getInstance() {
        return new StringContains("");
    }

    @Override
    protected Iterable<StringContains> getDifferentInstancesOrNull() {
        return Arrays.asList(
//                new StringContains(), Empty string and null have the same hashCode
                new StringContains("different"),
                new StringContains("", true)
        );
    }

    }
