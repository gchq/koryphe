/*
 * Copyright 2017-2020 Crown Copyright
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
import uk.gov.gchq.koryphe.util.CustomObj;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IsEqualTest extends PredicateTest {

    @Test
    public void shouldAcceptTheTestValue() {
        // Given
        final IsEqual filter = new IsEqual("test");

        // When
        boolean accepted = filter.test("test");

        // Then
        assertTrue(accepted);
    }

    @Test
    public void shouldAcceptWhenControlValueAndTestValueAreNull() {
        // Given
        final IsEqual filter = new IsEqual();

        // When
        boolean accepted = filter.test(null);

        // Then
        assertTrue(accepted);
    }

    @Test
    public void shouldRejectWhenNotEqual() {
        // Given
        final IsEqual filter = new IsEqual("test");

        // When
        boolean accepted = filter.test("a different value");

        // Then
        assertFalse(accepted);
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final CustomObj controlValue = new CustomObj();
        final IsEqual filter = new IsEqual(controlValue);

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.IsEqual\",%n" +
                "  \"value\" : {\"uk.gov.gchq.koryphe.util.CustomObj\":{\"value\":\"1\"}}%n" +
                "}"), json);

        // When 2
        final IsEqual deserialisedFilter = JsonSerialiser.deserialise(json, IsEqual.class);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(controlValue, deserialisedFilter.getControlValue());
    }

    @Override
    protected Class<IsEqual> getPredicateClass() {
        return IsEqual.class;
    }

    @Override
    protected IsEqual getInstance() {
        return new IsEqual("someString");
    }

    @Override
    protected Iterable<Predicate> getDifferentInstances() {
        return Arrays.asList(
                new IsEqual(),
                new IsEqual(4L)
        );
    }
}
