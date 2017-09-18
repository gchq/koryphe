/*
 * Copyright 2017 Crown Copyright
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

import org.junit.Test;
import uk.gov.gchq.koryphe.predicate.PredicateTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;
import java.io.IOException;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class StringContainsTest extends PredicateTest {
    private static final String VALUE1 = "This is a test string, used for the StringContains test";

    @Test
    public void shouldAcceptWhenStringInValue() {
        // Given
        final StringContains stringContains = new StringContains(VALUE1);

        // When
        boolean accepted = stringContains.test("used");

        // Then
        assertTrue(accepted);
    }

    @Test
    public void shouldRejectMismatchedCases() {
        // Given
        final StringContains stringContains = new StringContains(VALUE1);

        // When
        boolean accepted = stringContains.test("stringcontains");

        // Then
        assertFalse(accepted);
    }

    @Test
    public void shouldAcceptEmptyString() {
        // Given
        final StringContains stringContains = new StringContains(VALUE1);

        // When
        boolean accepted = stringContains.test("");

        // Then
        assertTrue(accepted);
    }

    @Test
    public void shouldRejectNullInput() {
        // Given
        final StringContains stringContains = new StringContains(VALUE1);

        // When
        boolean accepted = stringContains.test(null);

        // Then
        assertFalse(accepted);
    }

    @Test
    public void shouldMatchWhenIgnoreCaseSet() {
        // Given
        final StringContains stringContains = new StringContains(VALUE1);
        stringContains.setIgnoreCase(true);

        // When
        boolean accepted = stringContains.test("stringcontains");

        // Then
        assertTrue(accepted);
    }

    @Override
    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final StringContains filter = new StringContains(VALUE1);

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" + "  \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.StringContains\",%n" +
                                                          "  \"value\" : \"This is a test string, used for the StringContains test\",%n" +
                                                          "  \"ignoreCase\" : false%n" +
                                                          "}"), json);

        // When 2
        final StringContains deserialisedFilter = JsonSerialiser.deserialise(json, StringContains.class);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(VALUE1, deserialisedFilter.getValue());
    }

    @Override
    protected Predicate getInstance() {
        return new StringContains(VALUE1);
    }

    @Override
    protected Class<? extends Predicate> getPredicateClass() {
        return StringContains.class;
    }
}