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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IsXMoreThanYTest extends PredicateTest<IsXMoreThanY> {

    @Test
    public void shouldAcceptWhenMoreThan() {
        // Given
        final IsXMoreThanY filter = new IsXMoreThanY();

        // When
        boolean accepted = filter.test(2, 1);

        // Then
        assertTrue(accepted);
    }

    @Test
    public void shouldRejectTheValueWhenMoreThan() {
        // Given
        final IsXMoreThanY filter = new IsXMoreThanY();

        // When
        boolean accepted = filter.test(5, 6);

        // Then
        assertFalse(accepted);
    }

    @Test
    public void shouldRejectTheValueWhenEqualTo() {
        // Given
        final IsXMoreThanY filter = new IsXMoreThanY();

        // When
        boolean accepted = filter.test(5, 5);

        // Then
        assertFalse(accepted);
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final IsXMoreThanY filter = new IsXMoreThanY();

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.IsXMoreThanY\"%n" +
                "}"), json);

        // When 2
        final IsXMoreThanY deserialisedFilter = JsonSerialiser.deserialise(json, IsXMoreThanY.class);

        // Then 2
        assertNotNull(deserialisedFilter);
    }

    @Test
    public void shouldCheckInputClass() {
        // When
        final IsXLessThanY predicate = new IsXLessThanY();

        // Then
        assertTrue(predicate.isInputValid(Integer.class, Integer.class).isValid());
        assertTrue(predicate.isInputValid(String.class, String.class).isValid());

        assertFalse(predicate.isInputValid(Double.class).isValid());
        assertFalse(predicate.isInputValid(Integer.class, Double.class).isValid());
    }

    @Override
    protected IsXMoreThanY getInstance() {
        return new IsXMoreThanY();
    }

    @Override
    protected Iterable<IsXMoreThanY> getDifferentInstancesOrNull() {
        return null;
    }
}
